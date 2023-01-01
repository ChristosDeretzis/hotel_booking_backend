package com.service.hotel.booking.exception;

import com.service.hotel.booking.error.AuthErrorMessage;

public class UserExistsKeycloakException extends AuthenticationKeycloakException {
    public UserExistsKeycloakException(AuthErrorMessage authErrorMessage) {
        super(authErrorMessage);
    }
}
