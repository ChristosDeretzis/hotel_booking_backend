package com.deretzis.hotel_booking_backend.exception;

import com.deretzis.hotel_booking_backend.error.AuthErrorMessage;

public class GenericKeycloakException extends AuthenticationKeycloakException {
    public GenericKeycloakException(AuthErrorMessage authErrorMessage) {
        super(authErrorMessage);
    }
}
