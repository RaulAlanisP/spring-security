package com.spring.security.service;

import com.spring.security.dto.CreateUserDto;
import com.spring.security.models.EnumRole;
import com.spring.security.models.RoleEntity;
import com.spring.security.models.UserEntity;
import com.spring.security.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public UserEntity createUser(CreateUserDto createUserDto) {
        Set<RoleEntity> roles = createUserDto.roles().stream()
                .map(role -> RoleEntity.builder().name(EnumRole.valueOf(role)).build())
                .collect(Collectors.toSet());

        UserEntity userEntity = UserEntity.builder()
                .email(createUserDto.email())
                .username(createUserDto.username())
                .password(passwordEncoder.encode(createUserDto.password()))
                .roles(roles)
                .build();
        userRepository.save(userEntity);

        return userEntity;
    }

    @Override
    public boolean deleteUser(String id) {
        if (userRepository.existsById(Long.parseLong(id))) {
            userRepository.deleteById(Long.parseLong(id));
            return true;
        } else {
            return false;
        }
    }
}
