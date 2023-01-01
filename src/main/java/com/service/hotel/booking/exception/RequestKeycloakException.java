package com.service.hotel.booking.exception;

import com.service.hotel.booking.error.AuthErrorMessage;

public class RequestKeycloakException extends AuthenticationKeycloakException {
    public RequestKeycloakException(AuthErrorMessage authErrorMessage) {
        super(authErrorMessage);
    }
}
