package com.service.hotel.booking.exception;

import com.service.hotel.booking.error.AuthErrorMessage;

public class UnauthorizedKeycloakException extends AuthenticationKeycloakException {
    public UnauthorizedKeycloakException(AuthErrorMessage authErrorMessage) {
        super(authErrorMessage);
    }
}
