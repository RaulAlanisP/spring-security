package com.spring_security.utils;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilsTest {

    private JwtUtils jwtUtils;
    private static final String SECRET_KEY = "QW5kcmVzMTIzNDU2Nzg5QW5kcmVzMTIzNDU2Nzg5QW5kcmVzMTIzNDU2Nzg5";
    private static final String EXPIRATION = "3600000"; // 1 hora
    private static final String TEST_USERNAME = "testuser";

    @BeforeEach
    void setUp() {
        jwtUtils = new JwtUtils();
        ReflectionTestUtils.setField(jwtUtils, "secretKey", SECRET_KEY);
        ReflectionTestUtils.setField(jwtUtils, "expiration", EXPIRATION);
    }

    @Test
    void generateAccessToken_shouldCreateValidToken() {
        // Ejecutar
        String token = jwtUtils.generateAccessToken(TEST_USERNAME);

        // Verificar
        assertNotNull(token);
        assertTrue(jwtUtils.isTokenValid(token));
        assertEquals(TEST_USERNAME, jwtUtils.getUsernameFromToken(token));
    }

    @Test
    void isTokenValid_withInvalidToken_shouldReturnFalse() {
        // Preparar
        String invalidToken = "invalid.token.here";

        // Ejecutar y Verificar
        assertFalse(jwtUtils.isTokenValid(invalidToken));
    }

    @Test
    void getUsernameFromToken_shouldReturnCorrectUsername() {
        // Preparar
        String token = jwtUtils.generateAccessToken(TEST_USERNAME);

        // Ejecutar
        String username = jwtUtils.getUsernameFromToken(token);

        // Verificar
        assertEquals(TEST_USERNAME, username);
    }

    @Test
    void getClaimsFromToken_shouldReturnValidClaims() {
        // Preparar
        String token = jwtUtils.generateAccessToken(TEST_USERNAME);

        // Ejecutar
        Claims claims = jwtUtils.getClaimsFromToken(token);

        // Verificar
        assertNotNull(claims);
        assertEquals(TEST_USERNAME, claims.getSubject());
        assertNotNull(claims.getExpiration());
        assertTrue(claims.getExpiration().after(new Date()));
    }

    @Test
    void getClamFromToken_shouldExtractSpecificClaim() {
        // Preparar
        String token = jwtUtils.generateAccessToken(TEST_USERNAME);

        // Ejecutar
        String subject = jwtUtils.getClamFromToken(token, Claims::getSubject);
        Date expiration = jwtUtils.getClamFromToken(token, Claims::getExpiration);

        // Verificar
        assertEquals(TEST_USERNAME, subject);
        assertNotNull(expiration);
        assertTrue(expiration.after(new Date()));
    }

    @Test
    void getSignatureKey_shouldReturnValidKey() {
        // Ejecutar y Verificar
        assertNotNull(jwtUtils.getSignatureKey());
    }
}
