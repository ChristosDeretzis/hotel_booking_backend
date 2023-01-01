package com.service.hotel.booking.controller;

import com.service.hotel.booking.dto.CreateUserDto;
import com.service.hotel.booking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
