package com.deretzis.hotel_booking_backend.mapper;

import com.deretzis.hotel_booking_backend.dto.CreateUserDto;
import com.deretzis.hotel_booking_backend.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserEntity convert(CreateUserDto createUserDto) {
        return new UserEntity(
                createUserDto.getFirstName(),
                createUserDto.getLastName(),
                createUserDto.getEmail(),
                createUserDto.getUsername(),
                createUserDto.getPassword(),
                createUserDto.getDate_of_birth()
        );
    }
}
