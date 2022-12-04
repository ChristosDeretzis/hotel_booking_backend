package com.deretzis.hotel_booking_backend.exception;

import com.deretzis.hotel_booking_backend.error.AuthErrorMessage;

public class RequestKeycloakException extends AuthenticationKeycloakException {
    public RequestKeycloakException(AuthErrorMessage authErrorMessage) {
        super(authErrorMessage);
    }
}
