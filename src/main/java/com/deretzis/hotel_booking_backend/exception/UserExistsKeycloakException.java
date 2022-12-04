package com.deretzis.hotel_booking_backend.exception;

import com.deretzis.hotel_booking_backend.error.AuthErrorMessage;

public class UserExistsKeycloakException extends AuthenticationKeycloakException{
    public UserExistsKeycloakException(AuthErrorMessage authErrorMessage) {
        super(authErrorMessage);
    }
}
