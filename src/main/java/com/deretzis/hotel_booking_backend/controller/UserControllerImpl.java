package com.deretzis.hotel_booking_backend.controller;

import com.deretzis.hotel_booking_backend.dto.CreateUserDto;
import com.deretzis.hotel_booking_backend.dto.ServiceAccessTokenResponseDto;
import com.deretzis.hotel_booking_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserControllerImpl implements UserController {

    private final UserService userService;

    @Autowired
    public UserControllerImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void createNewUser(CreateUserDto createUserDto) {
        userService.createNewUser(createUserDto);
    }
}
