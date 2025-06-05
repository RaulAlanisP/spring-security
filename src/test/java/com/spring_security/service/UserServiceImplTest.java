package com.spring_security.service;

import com.spring_security.dto.CreateUserDto;
import com.spring_security.models.EnumRole;
import com.spring_security.models.UserEntity;
import com.spring_security.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        userService = new UserServiceImpl(userRepository, passwordEncoder);
    }

    @Test
    void createUser_shouldSaveUserWithEncodedPasswordAndRoles() {
        CreateUserDto dto = new CreateUserDto("test@example.com", "testuser", "password", Set.of("ADMIN"));
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");

        UserEntity result = userService.createUser(dto);

        ArgumentCaptor<UserEntity> captor = ArgumentCaptor.forClass(UserEntity.class);
        verify(userRepository).save(captor.capture());
        UserEntity saved = captor.getValue();

        assertEquals("test@example.com", saved.getEmail());
        assertEquals("testuser", saved.getUsername());
        assertEquals("encodedPassword", saved.getPassword());
        assertTrue(saved.getRoles().stream().anyMatch(r -> r.getName() == EnumRole.ADMIN));
        assertEquals(result, saved);
    }

    @Test
    void deleteUser_shouldReturnTrueWhenUserExists() {
        when(userRepository.existsById(1L)).thenReturn(true);

        boolean deleted = userService.deleteUser("1");

        verify(userRepository).deleteById(1L);
        assertTrue(deleted);
    }

    @Test
    void deleteUser_shouldReturnFalseWhenUserDoesNotExist() {
        when(userRepository.existsById(2L)).thenReturn(false);

        boolean deleted = userService.deleteUser("2");

        verify(userRepository, never()).deleteById(anyLong());
        assertFalse(deleted);
    }
}
