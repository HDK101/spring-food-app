package com.eisen.foodapp.common.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.eisen.foodapp.module.user.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {
    @Value("${security.jwt.secret}")
    private String secret;

    @Value("${spring.application.name}")
    private String appName;

    public String generateToken(User user) {
        try {
            String token = JWT.create()
                    .withIssuer(appName)
                    .withSubject(user.getLogin())
                    .withExpiresAt(this.generateExpirationDate())
                    .sign(this.defaultAlgorithm());
            return token;
        } catch (JWTCreationException ex) {
            throw new RuntimeException("Could not create JWT token", ex);
        }
    }

    public String validateToken(String token) {
        try {
            return JWT.require(this.defaultAlgorithm())
                    .withIssuer(appName)
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException ex) {
            return "";
        }
    }

    private Algorithm defaultAlgorithm() {
        return Algorithm.HMAC256(secret);
    }

    private Instant generateExpirationDate() {
        return LocalDateTime.now().plusHours(1).toInstant(ZoneOffset.of("-03:00"));
    }
}
