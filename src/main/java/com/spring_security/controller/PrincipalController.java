package com.spring_security.controller;

import com.spring_security.dto.CreateUserDto;
import com.spring_security.models.UserEntity;
import com.spring_security.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class PrincipalController {

    private final UserService userService;

    public PrincipalController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/notsecured")
    public String getPrincipal() {
        return "Hello, this is the principal endpoint not secured!";
    }

    @GetMapping("/secured")
    public String getPrincipal2() {
        return "Hello, this is the principal endpoint secured!";
    }

    @PostMapping("/user")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserEntity> createUser(@Valid @RequestBody CreateUserDto createUserDto) {
        UserEntity userEntity = userService.createUser(createUserDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(userEntity);
    }

    @DeleteMapping("/user")
    public ResponseEntity<String> deleteUser(@RequestParam String id) {
        if (userService.deleteUser(id)) {
            return ResponseEntity.ok("User deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }
}
