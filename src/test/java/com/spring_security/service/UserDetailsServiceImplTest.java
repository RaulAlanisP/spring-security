package com.spring_security.service;

import com.spring_security.models.EnumRole;
import com.spring_security.models.RoleEntity;
import com.spring_security.models.UserEntity;
import com.spring_security.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserDetailsServiceImplTest {

    private UserRepository userRepository;
    private UserDetailsServiceImpl userDetailsService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userDetailsService = new UserDetailsServiceImpl(userRepository);
    }

    @Test
    void loadUserByUsername_whenUserExists_shouldReturnUserDetails() {
        // Preparar
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("testuser");
        userEntity.setPassword("password");

        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setName(EnumRole.USER);
        userEntity.setRoles(Set.of(roleEntity));

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(userEntity));

        // Ejecutar
        UserDetails result = userDetailsService.loadUserByUsername("testuser");

        // Verificar
        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        assertEquals("password", result.getPassword());
        assertTrue(result.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(auth -> auth.equals("ROLE_USER")));
        verify(userRepository).findByUsername("testuser");
    }

    @Test
    void loadUserByUsername_whenUserNotFound_shouldThrowException() {
        // Preparar
        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        // Ejecutar y Verificar
        Exception exception = assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername("nonexistent");
        });

        assertEquals("User not found with username: nonexistent", exception.getMessage());
        verify(userRepository).findByUsername("nonexistent");
    }

    @Test
    void loadUserByUsername_withMultipleRoles_shouldMapAllRoles() {
        // Preparar
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("admin");
        userEntity.setPassword("password");

        RoleEntity userRole = new RoleEntity();
        userRole.setName(EnumRole.USER);
        RoleEntity adminRole = new RoleEntity();
        adminRole.setName(EnumRole.ADMIN);
        userEntity.setRoles(Set.of(userRole, adminRole));

        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(userEntity));

        // Ejecutar
        UserDetails result = userDetailsService.loadUserByUsername("admin");

        // Verificar
        assertNotNull(result);
        assertEquals(2, result.getAuthorities().size());
        assertTrue(result.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(auth -> auth.equals("ROLE_USER")));
        assertTrue(result.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(auth -> auth.equals("ROLE_ADMIN")));
    }
}
