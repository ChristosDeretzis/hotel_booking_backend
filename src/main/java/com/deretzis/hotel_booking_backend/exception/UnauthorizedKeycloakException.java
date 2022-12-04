package com.deretzis.hotel_booking_backend.exception;

import com.deretzis.hotel_booking_backend.error.AuthErrorMessage;

public class UnauthorizedKeycloakException extends AuthenticationKeycloakException {
    public UnauthorizedKeycloakException(AuthErrorMessage authErrorMessage) {
        super(authErrorMessage);
    }
}
