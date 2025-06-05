package com.spring_security.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.Set;

public record CreateUserDto(
        @Email @NotBlank String email,
        @NotBlank String username,
        @NotBlank String password,
        Set<String> roles) {
    // This record can be used to transfer user creation data
    // It automatically generates the constructor, getters, equals, hashCode, and toString methods
}
