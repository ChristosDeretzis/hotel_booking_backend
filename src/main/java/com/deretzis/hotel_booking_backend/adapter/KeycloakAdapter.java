package com.deretzis.hotel_booking_backend.adapter;

import com.deretzis.hotel_booking_backend.keycloak.KeycloakCreateUserRequestDto;
import com.deretzis.hotel_booking_backend.keycloak.ServiceAccessTokenResponseDto;
import com.deretzis.hotel_booking_backend.keycloak.UserRepresentation;

public interface KeycloakAdapter {
    ServiceAccessTokenResponseDto obtainServiceAccessToken();
    String createKeycloakUser(KeycloakCreateUserRequestDto keycloakCreateUserRequestDto);
    UserRepresentation getKeycloakUserByEmail(String email);
}
