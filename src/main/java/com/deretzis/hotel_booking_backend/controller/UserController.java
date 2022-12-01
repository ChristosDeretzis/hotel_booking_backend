package com.deretzis.hotel_booking_backend.controller;

import com.deretzis.hotel_booking_backend.dto.CreateUserDto;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/users")
public interface UserController {

    @PostMapping("/signup")
    void createNewUser(@RequestBody CreateUserDto createUserDto);
}
