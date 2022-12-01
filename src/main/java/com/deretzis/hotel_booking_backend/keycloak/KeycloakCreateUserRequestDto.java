package com.deretzis.hotel_booking_backend.keycloak;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KeycloakCreateUserRequestDto {
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private Boolean enabled;
    private List<KeycloakPasswordCredentials> credentials;
}
