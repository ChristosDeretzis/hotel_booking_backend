package com.service.hotel.booking.exception;

import com.service.hotel.booking.error.AuthErrorMessage;

public class GenericKeycloakException extends AuthenticationKeycloakException {
    public GenericKeycloakException(AuthErrorMessage authErrorMessage) {
        super(authErrorMessage);
    }
}
