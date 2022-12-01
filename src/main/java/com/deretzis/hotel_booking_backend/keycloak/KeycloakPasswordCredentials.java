package com.deretzis.hotel_booking_backend.keycloak;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KeycloakPasswordCredentials {
    private String type;
    private String value;
    private Boolean temporary;
}
