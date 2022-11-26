package com.deretzis.hotel_booking_backend.service;

import com.deretzis.hotel_booking_backend.dto.ServiceAccessTokenResponseDto;
import org.springframework.stereotype.Service;

public interface UserService {
    ServiceAccessTokenResponseDto getServiceAccessToken();
}
