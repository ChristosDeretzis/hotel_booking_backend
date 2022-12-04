package com.deretzis.hotel_booking_backend.error;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthErrorMessage {

    private Integer code;
    private String message;
}
