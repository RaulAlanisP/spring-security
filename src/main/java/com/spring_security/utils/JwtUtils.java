package com.spring_security.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Component
@Slf4j
public class JwtUtils {

    @Value("${jwt.secret.key}")
    private String secretKey;

    @Value("${jwt.time.expiration}")
    private String expiration;

    // Generates a JWT access token for the given username
    public String generateAccessToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(expiration)))
                .signWith(getSignatureKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Validates the JWT token and returns the username if valid
    public boolean isTokenValid(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(getSignatureKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return true;
        } catch (Exception e) {
            log.error("JWT token validation failed: {}", e.getMessage());
            return false; // Token is invalid
        }
    }

    // Extracts clams from the JWT token
    public Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(getSignatureKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Extracts one clam from the JWT token
    public <T> T getClamFromToken(String token, Function<Claims, T> claimsTFunction) {
        Claims claims = getClaimsFromToken(token);
        return claimsTFunction.apply(claims);
    }

    // Extracts the username from the JWT token
    public String getUsernameFromToken(String token) {
        return getClamFromToken(token, Claims::getSubject);
    }

    // Gets the signature key for signing JWT tokens
    public Key getSignatureKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
