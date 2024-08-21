package com.example.dormitoryspring.services;

import com.example.dormitoryspring.dto.request.IntrospectRequest;
import com.example.dormitoryspring.dto.request.UserRequest;
import com.example.dormitoryspring.dto.request.RefreshRequest;
import com.example.dormitoryspring.dto.response.*;
import com.example.dormitoryspring.entity.InvalidatedToken;
import com.example.dormitoryspring.entity.User;
import com.example.dormitoryspring.enums.Role;
import com.example.dormitoryspring.exception.AppException;
import com.example.dormitoryspring.exception.ErrorCode;
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
import java.util.Optional;
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
            throw new AppException(ErrorCode.USER_EXISTED);
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

    public Optional<LoginResponse> getCurrentUserLogin(String email){
            Optional<User> userOPT = userRepository.findByEmail(email);
        if (userOPT.isEmpty()) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        User user = userOPT.get();
        LoginResponse loginResponse = LoginResponse.builder()
                .id_user(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .password(user.getPassword())
                .id_student(user.getStudent() != null ? user.getStudent().getId_student() : null)
                .role(user.getRole())
                .build();
        return Optional.of(loginResponse);
    }


    public LoginResponse login(UserRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new AppException(ErrorCode.INVALID_PASSWORD);
        }

        String accessToken = generateToken(user);

        return LoginResponse.builder()
                .id_user(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .password(user.getPassword())
                // Check if user.getStudent() is not null and set id_student if it's available
                .id_student(user.getStudent() != null ? user.getStudent().getId_student() : null)
                .role(user.getRole())
                .token(accessToken)
                .build();
    }

    public void logout(String token) throws ParseException, JOSEException {
        try {
            SignedJWT signedJWT = verifyToken(token);

            String jwtId = signedJWT.getJWTClaimsSet().getJWTID();
            Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

            InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                    .id(jwtId)
                    .expiryTime(expiryTime)
                    .build();

            invalidatedTokenRepository.save(invalidatedToken);
        } catch (AppException ex) {
            log.info("Token already expired or invalid: {}", ex.getMessage());
            throw new AppException(ex.getErrorCode());
        } catch (Exception ex) {
            // Catch any unexpected exceptions
            log.error("Unexpected error during logout: {}", ex.getMessage());
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION, ex);
        }
    }

    // this for check token valid or not (correct=true ; false = incorrect)
    public IntrospectResponse introspect(IntrospectRequest request)
            throws JOSEException, ParseException {
        var token = request.getToken();
        boolean isValid = true;

        try {
            verifyToken(token);
        } catch (AppException e) {
            isValid = false;
        }

        return IntrospectResponse.builder()
                .valid(isValid)
                .build();
    }

    public RefreshResponse refreshToken(RefreshRequest request) throws ParseException, JOSEException {
        // Verify the old token
        SignedJWT signedJWT = verifyToken(request.getToken());

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
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        // Generate a new token
        String newToken = generateToken(user);

        return RefreshResponse.builder()
                .token(newToken)
                .message("Token refreshed successfully")
                .build();
    }

    private SignedJWT verifyToken(String token) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

        // Parse the token
        SignedJWT signedJWT = SignedJWT.parse(token);

        // Retrieve and check the token's expiration time
        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        // Verify the token's signature and check expiration
        boolean verified = signedJWT.verify(verifier);
        if (!(verified && expiryTime.after(new Date()))) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        // Check if the token has been invalidated
        if (invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID())) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
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
