package com.service.hotel.booking.exception;

import com.service.hotel.booking.error.AuthErrorMessage;

public class AuthenticationKeycloakException extends RuntimeException {
    private final AuthErrorMessage authErrorMessage;

    public AuthenticationKeycloakException(AuthErrorMessage authErrorMessage) {
        this.authErrorMessage = authErrorMessage;
    }

    public AuthErrorMessage getAuthErrorMessage() {
        return authErrorMessage;
    }
}
