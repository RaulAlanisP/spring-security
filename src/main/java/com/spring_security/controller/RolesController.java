package com.spring_security.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class RolesController {

    @GetMapping("/admin")
    public String accessAdmin() {
        return "Access granted to admin";
    }

    @GetMapping("/user")
    public String accessUser() {
        return "Access granted to user";
    }

    @GetMapping("/invited")
    public String accessInvited() {
        return "Access granted to invited";
    }
}
