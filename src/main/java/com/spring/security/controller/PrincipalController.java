package com.spring.security.controller;

import com.spring.security.dto.CreateUserDto;
import com.spring.security.models.EnumRole;
import com.spring.security.models.RoleEntity;
import com.spring.security.models.UserEntity;
import com.spring.security.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class PrincipalController {

    @Autowired
    private UserRepository userRepository;

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
    public ResponseEntity<?> createUser(@Valid @RequestBody CreateUserDto createUserDto) {

        Set<RoleEntity> roles = createUserDto.roles().stream()
                .map(role -> RoleEntity.builder().name(EnumRole.valueOf(role)).build())
                .collect(Collectors.toSet());

        UserEntity userEntity = UserEntity.builder()
                .email(createUserDto.email())
                .username(createUserDto.username())
                .password(createUserDto.password())
                .roles(roles)
                .build();

        userRepository.save(userEntity);

        return ResponseEntity.status(HttpStatus.CREATED).body(userEntity);
    }

    @DeleteMapping("/user")
    public ResponseEntity<?> deleteUser(@RequestParam String id) {
        if (userRepository.existsById(Long.parseLong(id))) {
            userRepository.deleteById(Long.parseLong(id));
            return ResponseEntity.ok("User deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }
}
