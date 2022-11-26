package com.deretzis.hotel_booking_backend.service;

import com.deretzis.hotel_booking_backend.adapter.KeycloakAdapter;
import com.deretzis.hotel_booking_backend.dto.ServiceAccessTokenResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    KeycloakAdapter keycloakAdapter;
    @Override
    public ServiceAccessTokenResponseDto getServiceAccessToken() {
        return keycloakAdapter.obtainServiceAccessToken();
    }
}
