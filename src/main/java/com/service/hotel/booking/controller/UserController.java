package com.service.hotel.booking.controller;

import com.service.hotel.booking.dto.CreateUserDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/users")
public interface UserController {

    @PostMapping("/signup")
    void createNewUser(@RequestBody CreateUserDto createUserDto);
}
