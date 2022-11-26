package com.deretzis.hotel_booking_backend.adapter;

import com.deretzis.hotel_booking_backend.dto.ServiceAccessTokenResponseDto;
import org.springframework.stereotype.Component;

public interface KeycloakAdapter {
    ServiceAccessTokenResponseDto obtainServiceAccessToken();
}
