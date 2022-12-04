package com.deretzis.hotel_booking_backend.error;

public enum AuthErrorCodes {

    GENERIC_ERROR(0, "The request could not be served"),
    INVALID_ACCESS_TOKEN_ERROR(1, "Wrong or Expiring access_token"),
    USER_EXISTS_ERROR(2, "User has the same username or password"),
    REQUEST_ERROR(3, "Error in the request body");

    private final Integer code;
    private final String message;

    AuthErrorCodes(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
