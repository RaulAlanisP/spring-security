package com.spring_security.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring_security.models.UserEntity;
import com.spring_security.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.DelegatingServletInputStream;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class JwtAuthenticationFilterTest {

    private JwtUtils jwtUtils;
    private JwtAuthenticationFilter filter;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private AuthenticationManager authManager;
    private FilterChain filterChain;

    @BeforeEach
    void setUp() {
        jwtUtils = mock(JwtUtils.class);
        authManager = mock(AuthenticationManager.class);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        filterChain = mock(FilterChain.class);

        filter = new JwtAuthenticationFilter(jwtUtils);
        filter.setAuthenticationManager(authManager);
    }

    @Test
    void attemptAuthentication_shouldAuthenticate() throws Exception {
        // Preparar
        UserEntity user = new UserEntity();
        user.setUsername("testuser");
        user.setPassword("password");

        String json = new ObjectMapper().writeValueAsString(user);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(json.getBytes());

        when(request.getInputStream()).thenReturn(new DelegatingServletInputStream(inputStream));
        when(authManager.authenticate(any())).thenReturn(
                new UsernamePasswordAuthenticationToken("testuser", "password")
        );

        // Ejecutar
        Authentication result = filter.attemptAuthentication(request, response);

        // Verificar
        assertNotNull(result);
        verify(authManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void attemptAuthentication_shouldThrowException_whenInvalidJson() {
        // Preparar
        try {
            when(request.getInputStream()).thenThrow(new IOException());

            // Ejecutar
            filter.attemptAuthentication(request, response);
            fail("Debería haber lanzado AuthenticationException");
        } catch (Exception e) {
            // Verificar
            assertEquals("Authentication failed", e.getMessage());
        }
    }

    @Test
    void successfulAuthentication_shouldGenerateToken() throws Exception {
        // Preparar
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        User user = new User("testuser", "password", Collections.emptyList());
        Authentication auth = new UsernamePasswordAuthenticationToken(user, null);
        when(jwtUtils.generateAccessToken("testuser")).thenReturn("test-token");

        // Ejecutar
        filter.successfulAuthentication(request, response, filterChain, auth);

        // Verificar
        verify(response).setHeader("Authorization", "test-token");
        verify(jwtUtils).generateAccessToken("testuser");
        verify(response).setContentType("application/json");

        String responseBody = stringWriter.toString();
        assertTrue(responseBody.contains("test-token"));
        assertTrue(responseBody.contains("testuser"));
    }
}
