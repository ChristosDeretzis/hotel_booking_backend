package com.deretzis.hotel_booking_backend.advice;

import com.deretzis.hotel_booking_backend.exception.GenericKeycloakException;
import com.deretzis.hotel_booking_backend.exception.RequestKeycloakException;
import com.deretzis.hotel_booking_backend.exception.UnauthorizedKeycloakException;
import com.deretzis.hotel_booking_backend.exception.UserExistsKeycloakException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ControllerExceptionAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(GenericKeycloakException.class)
    public ResponseEntity<Object> handleGenericKeycloakException(GenericKeycloakException exception, WebRequest request) {
        return handleExceptionInternal(exception, exception.getAuthErrorMessage(),
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(RequestKeycloakException.class)
    public ResponseEntity<Object> handleRequestKeycloakException(RequestKeycloakException exception, WebRequest request) {
        return handleExceptionInternal(exception, exception.getAuthErrorMessage(),
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(UnauthorizedKeycloakException.class)
    public ResponseEntity<Object> handleGenericKeycloakException(UnauthorizedKeycloakException exception, WebRequest request) {
        return handleExceptionInternal(exception, exception.getAuthErrorMessage(),
                new HttpHeaders(), HttpStatus.UNAUTHORIZED, request);
    }

    @ExceptionHandler(UserExistsKeycloakException.class)
    public ResponseEntity<Object> handleGenericKeycloakException(UserExistsKeycloakException exception, WebRequest request) {
        return handleExceptionInternal(exception, exception.getAuthErrorMessage(),
                new HttpHeaders(), HttpStatus.CONFLICT, request);
    }
}
