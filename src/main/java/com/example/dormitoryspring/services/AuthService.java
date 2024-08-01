package com.example.dormitoryspring.services;

import com.example.dormitoryspring.dto.request.UserRequest;
import com.example.dormitoryspring.dto.request.RefreshRequest;
import com.example.dormitoryspring.dto.response.LoginResponse;
import com.example.dormitoryspring.dto.response.UserResponse;
import com.example.dormitoryspring.dto.response.RefreshResponse;
import com.example.dormitoryspring.dto.response.RegisterResponse;
import com.example.dormitoryspring.entity.InvalidatedToken;
import com.example.dormitoryspring.entity.User;
import com.example.dormitoryspring.enums.Role;
import com.example.dormitoryspring.exception.AppException;
import com.example.dormitoryspring.repositories.InvalidatedTokenRepository;
import com.example.dormitoryspring.repositories.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.experimental.NonFinal;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

@Service
@Slf4j
public class AuthService {
    @Autowired
    private UserRepository userRepository ;

    @Autowired
    private InvalidatedTokenRepository invalidatedTokenRepository;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    @Value("${valid-duration}")
    private long VALID_DURATION;

    @Value("${refreshable-duration}")
    private long REFRESHABLE_DURATION;

    public RegisterResponse register(UserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AppException("User already exists");
        }

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole() != null ? request.getRole() : Role.USER)
                .build();

        // Save the user to the db
        userRepository.save(user);

        return RegisterResponse.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .password(user.getPassword())
                .role(user.getRole())
                .build();
    }

    public LoginResponse login(UserRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AppException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new AppException("Invalid credentials");
        }

        String accessToken = generateToken(user);

        // Build the response DTO
        return LoginResponse.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .role(user.getRole())
                .token(accessToken)
                .build();
    }

    public void logout(String token) throws ParseException, JOSEException {
        try {
            SignedJWT signedJWT = verifyToken(token, false);

            String jwtId = signedJWT.getJWTClaimsSet().getJWTID();
            Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

            InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                    .id(jwtId)
                    .expiryTime(expiryTime)
                    .build();

            invalidatedTokenRepository.save(invalidatedToken);
        } catch (AppException ex) {
            log.info("Token already expired or invalid: {}", ex.getMessage());
            throw new AppException(ex.getMessage());
        }
    }


    public RefreshResponse refreshToken(RefreshRequest request) throws ParseException, JOSEException {
        // Verify the old token
        SignedJWT signedJWT = verifyToken(request.getToken(), true);

        // Get the JWT ID and expiry time from the old token
        String jwtId = signedJWT.getJWTClaimsSet().getJWTID();
        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        // Invalidate the old token by saving it to the repository
        InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                .id(jwtId)
                .expiryTime(expiryTime)
                .build();
        invalidatedTokenRepository.save(invalidatedToken);

        // Get user email from the old token
        String email = signedJWT.getJWTClaimsSet().getSubject();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException("User not found"));

        // Generate a new token
        String newToken = generateToken(user);

        return RefreshResponse.builder()
                .token(newToken)
                .message("Token refreshed successfully")
                .build();
    }



    private SignedJWT verifyToken(String token, boolean isRefresh) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);

        // Verify the signature
        boolean verified = signedJWT.verify(verifier);
        if (!verified) {
            throw new AppException("Invalid token signature");
        }

        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        if (isRefresh) {
            expiryTime = Date.from(signedJWT.getJWTClaimsSet().getIssueTime().toInstant()
                    .plus(REFRESHABLE_DURATION, ChronoUnit.SECONDS));
        }

        // Check token expiration
        if (expiryTime.before(new Date())) {
            throw new AppException("Token expired");
        }

        // Check if the token is invalidated
        if (invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID())) {
            throw new AppException("Token is invalidated");
        }

        return signedJWT;
    }

    private String generateToken(User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getEmail())
                .issuer("vengann.com")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(VALID_DURATION, ChronoUnit.SECONDS).toEpochMilli()))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", buildScope(user))
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Cannot create token", e);
            throw new RuntimeException(e);
        }
    }

    private String buildScope(User user) {
        // Here you can customize the claims (permissions or roles) based on your application needs
        return user.getRole().name();
    }
}
