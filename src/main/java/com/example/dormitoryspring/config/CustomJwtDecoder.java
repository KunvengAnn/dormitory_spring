package com.example.dormitoryspring.config;

import com.example.dormitoryspring.repositories.InvalidatedTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;

@Component
public class CustomJwtDecoder implements JwtDecoder {

    @Value("${jwt.signerKey}")
    private String signerKey;

    private final InvalidatedTokenRepository invalidatedTokenRepository;

    @Autowired
    public CustomJwtDecoder(InvalidatedTokenRepository invalidatedTokenRepository) {
        this.invalidatedTokenRepository = invalidatedTokenRepository;
    }

    @Override
    public Jwt decode(String token) throws JwtException {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(signerKey.getBytes(), "HS512");
            NimbusJwtDecoder nimbusJwtDecoder = NimbusJwtDecoder.withSecretKey(secretKeySpec)
                    .macAlgorithm(MacAlgorithm.HS512)
                    .build();

            // Decode the JWT token
            Jwt jwt = nimbusJwtDecoder.decode(token);

            // Check if the token has been invalidated (e.g., on logout or expired ..... )
            if (invalidatedTokenRepository.existsById(jwt.getId())) {
                throw new JwtException("Token is invalidated");
            }

            return nimbusJwtDecoder.decode(token);
        } catch (Exception e) {
            throw new JwtException("Invalid JWT token", e);
        }
    }
}

