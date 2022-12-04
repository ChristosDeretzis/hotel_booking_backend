package com.deretzis.hotel_booking_backend.exception;

import com.deretzis.hotel_booking_backend.error.AuthErrorMessage;

public class AuthenticationKeycloakException extends RuntimeException {
    private final AuthErrorMessage authErrorMessage;

    public AuthenticationKeycloakException(AuthErrorMessage authErrorMessage) {
        this.authErrorMessage = authErrorMessage;
    }

    public AuthErrorMessage getAuthErrorMessage() {
        return authErrorMessage;
    }
}
