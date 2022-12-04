package com.deretzis.hotel_booking_backend.service;

import com.deretzis.hotel_booking_backend.dto.CreateUserDto;

public interface UserService {

    void createNewUser(CreateUserDto createUserDto);
}
