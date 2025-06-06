package com.spring_security.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RolesControllerTest {

    private RolesController rolesController;

    @BeforeEach
    void setUp() {
        rolesController = new RolesController();
    }

    @Test
    void accessAdmin_shouldReturnAdminMessage() {
        String result = rolesController.accessAdmin();
        assertEquals("Access granted to admin", result);
    }

    @Test
    void accessUser_shouldReturnUserMessage() {
        String result = rolesController.accessUser();
        assertEquals("Access granted to user", result);
    }

    @Test
    void accessInvited_shouldReturnInvitedMessage() {
        String result = rolesController.accessInvited();
        assertEquals("Access granted to invited", result);
    }
}
