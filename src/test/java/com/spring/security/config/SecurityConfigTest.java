package com.spring.security.config;

import com.spring.security.security.JwtAuthorizationFilter;
import com.spring.security.utils.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;

class SecurityConfigTest {

    private UserDetailsService userDetailsService;
    private JwtUtils jwtUtils;
    private JwtAuthorizationFilter jwtAuthorizationFilter;
    private SecurityConfig securityConfig;

    @BeforeEach
    void setUp() {
        userDetailsService = mock(UserDetailsService.class);
        jwtUtils = mock(JwtUtils.class);
        jwtAuthorizationFilter = mock(JwtAuthorizationFilter.class);
        securityConfig = new SecurityConfig(userDetailsService, jwtUtils, jwtAuthorizationFilter);
    }

    @Test
    void passwordEncoderBeanShouldReturnBCryptPasswordEncoder() {
        PasswordEncoder encoder = securityConfig.passwordEncoder();
        assertNotNull(encoder);
        assertTrue(encoder.matches("test", encoder.encode("test")));
    }

    @Test
    void securityFilterChainShouldNotBeNull() throws Exception {
        HttpSecurity httpSecurity = mock(HttpSecurity.class, RETURNS_DEEP_STUBS);
        AuthenticationManager authenticationManager = mock(AuthenticationManager.class);
        SecurityFilterChain chain = securityConfig.securityFilterChain(httpSecurity, authenticationManager);
        assertNotNull(chain);
    }

}
