package com.spring_security.security;

import com.spring_security.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.io.IOException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtAuthorizationFilterTest {

    private JwtUtils jwtUtils;
    private UserDetailsService userDetailsService;
    private JwtAuthorizationFilter filter;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private FilterChain filterChain;

    @BeforeEach
    void setUp() {
        jwtUtils = mock(JwtUtils.class);
        userDetailsService = mock(UserDetailsService.class);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        filterChain = mock(FilterChain.class);
        filter = new JwtAuthorizationFilter(jwtUtils, userDetailsService);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void doFilterInternal_withValidToken_shouldSetAuthentication() throws ServletException, IOException {
        // Preparar
        String token = "valid-token";
        String bearerToken = "Bearer " + token;
        String username = "testuser";
        UserDetails userDetails = new User(username, "", Collections.emptyList());

        when(request.getHeader("Authorization")).thenReturn(bearerToken);
        when(jwtUtils.isTokenValid(token)).thenReturn(true);
        when(jwtUtils.getUsernameFromToken(token)).thenReturn(username);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);

        // Ejecutar
        filter.doFilterInternal(request, response, filterChain);

        // Verificar
        verify(filterChain).doFilter(request, response);
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals(username, SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    }

    @Test
    void doFilterInternal_withNoToken_shouldNotSetAuthentication() throws ServletException, IOException {
        // Preparar
        when(request.getHeader("Authorization")).thenReturn(null);

        // Ejecutar
        filter.doFilterInternal(request, response, filterChain);

        // Verificar
        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void doFilterInternal_withNonBearerToken_shouldNotSetAuthentication() throws ServletException, IOException {
        // Preparar
        when(request.getHeader("Authorization")).thenReturn("Basic xyz");

        // Ejecutar
        filter.doFilterInternal(request, response, filterChain);

        // Verificar
        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
}
