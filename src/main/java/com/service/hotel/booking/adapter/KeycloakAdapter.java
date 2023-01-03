package com.service.hotel.booking.adapter;

import com.service.hotel.booking.keycloak.KeycloakCreateUserRequestDto;

public interface KeycloakAdapter {
    String createKeycloakUser(KeycloakCreateUserRequestDto keycloakCreateUserRequestDto);
}
