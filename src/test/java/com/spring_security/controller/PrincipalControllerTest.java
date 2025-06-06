package com.spring_security.controller;

import com.spring_security.dto.CreateUserDto;
import com.spring_security.models.UserEntity;
import com.spring_security.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class PrincipalControllerTest {

    private UserService userService;
    private PrincipalController principalController;

    @BeforeEach
    void setUp() {
        userService = mock(UserService.class);
        principalController = new PrincipalController(userService);
    }

    @Test
    void getPrincipal_shouldReturnNotSecuredMessage() {
        String result = principalController.getPrincipal();
        assertEquals("Hello, this is the principal endpoint not secured!", result);
    }

    @Test
    void getPrincipal2_shouldReturnSecuredMessage() {
        String result = principalController.getPrincipal2();
        assertEquals("Hello, this is the principal endpoint secured!", result);
    }

    @Test
    void createUser_shouldReturnCreatedUser() {
        CreateUserDto dto = new CreateUserDto(
                "example@example.com",
                "exampleUser",
                "1234",
                Set.of("ROLE_USER"));
        UserEntity userEntity = new UserEntity();
        when(userService.createUser(ArgumentMatchers.any(CreateUserDto.class))).thenReturn(userEntity);

        ResponseEntity<UserEntity> response = principalController.createUser(dto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(userEntity, response.getBody());
        verify(userService, times(1)).createUser(dto);
    }

    @Test
    void deleteUser_shouldReturnOkWhenUserDeleted() {
        when(userService.deleteUser("1")).thenReturn(true);

        ResponseEntity<String> response = principalController.deleteUser("1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User deleted successfully", response.getBody());
        verify(userService, times(1)).deleteUser("1");
    }

    @Test
    void deleteUser_shouldReturnNotFoundWhenUserNotDeleted() {
        when(userService.deleteUser("2")).thenReturn(false);

        ResponseEntity<String> response = principalController.deleteUser("2");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User not found", response.getBody());
        verify(userService, times(1)).deleteUser("2");
    }
}
