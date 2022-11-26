package com.deretzis.hotel_booking_backend.controller;

import com.deretzis.hotel_booking_backend.dto.ServiceAccessTokenResponseDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/users")
public interface UserController {

    @GetMapping("/service-token")
    ServiceAccessTokenResponseDto getServiceAccessToken();
}
