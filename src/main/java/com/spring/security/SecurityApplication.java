package com.spring.security;

import com.spring.security.models.EnumRole;
import com.spring.security.models.RoleEntity;
import com.spring.security.models.UserEntity;
import com.spring.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@SpringBootApplication
public class SecurityApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecurityApplication.class, args);
    }

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository;

    @Bean
    CommandLineRunner init() {
        return args -> {
            // Initialization logic can go here if needed
            UserEntity adminUser = UserEntity.builder()
                    .username("raul")
                    .password(passwordEncoder.encode("1234")) // In a real application, use a secure password encoder
                    .email("raul@example.com")
                    .roles(Set.of(RoleEntity.builder()
                            .name(EnumRole.valueOf(EnumRole.ADMIN.name()))
                            .build()))
                    .build();

            UserEntity daniUser = UserEntity.builder()
                    .username("dani")
                    .password(passwordEncoder.encode("1234")) // In a real application, use a secure password encoder
                    .email("dani@example.com")
                    .roles(Set.of(RoleEntity.builder()
                            .name(EnumRole.valueOf(EnumRole.USER.name()))
                            .build()))
                    .build();

            UserEntity invitadoUser = UserEntity.builder()
                    .username("invitado")
                    .password(passwordEncoder.encode("1234")) // In a real application, use a secure password encoder
                    .email("invitado@example.com")
                    .roles(Set.of(RoleEntity.builder()
                            .name(EnumRole.valueOf(EnumRole.INVITED.name()))
                            .build()))
                    .build();

            userRepository.save(adminUser);
            userRepository.save(daniUser);
            userRepository.save(invitadoUser);

            System.out.println("Security Application has started successfully!");
        };
    }
}
