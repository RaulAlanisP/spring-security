package com.spring.security.service;

import com.spring.security.dto.CreateUserDto;
import com.spring.security.models.UserEntity;

public interface UserService {
    /**
     * Creates a new user with the given details.
     *
     * @param createUserDto the details of the user to be created
     * @return the created user entity
     */
    UserEntity createUser(CreateUserDto createUserDto);

    /**
     * Deletes a user by their ID.
     *
     * @param id the ID of the user to be deleted
     * @return true if the user was deleted, false otherwise
     */
    boolean deleteUser(String id);
}
