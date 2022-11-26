package com.deretzis.hotel_booking_backend.controller;

import com.deretzis.hotel_booking_backend.dto.ServiceAccessTokenResponseDto;
import com.deretzis.hotel_booking_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserControllerImpl implements UserController {

    @Autowired
    UserService userService;

    @Override
    public ServiceAccessTokenResponseDto getServiceAccessToken() {
        return userService.getServiceAccessToken();
    }
}
