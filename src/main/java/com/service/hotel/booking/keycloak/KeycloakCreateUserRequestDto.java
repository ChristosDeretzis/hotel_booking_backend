package com.service.hotel.booking.keycloak;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
