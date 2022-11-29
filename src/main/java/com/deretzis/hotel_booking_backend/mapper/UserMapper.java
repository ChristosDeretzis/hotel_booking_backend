package com.deretzis.hotel_booking_backend.mapper;

import com.deretzis.hotel_booking_backend.dto.CreateUserDto;
import com.deretzis.hotel_booking_backend.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserMapper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public UserEntity convert(CreateUserDto createUserDto) {
        return new UserEntity(
                createUserDto.getFirstName(),
                createUserDto.getLastName(),
                createUserDto.getEmail(),
                createUserDto.getUsername(),
                passwordEncoder.encode(createUserDto.getPassword()),
                createUserDto.getDate_of_birth()
        );
    }
}
