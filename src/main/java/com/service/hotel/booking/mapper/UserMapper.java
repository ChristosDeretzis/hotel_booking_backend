package com.service.hotel.booking.mapper;

import com.service.hotel.booking.dto.CreateUserDto;
import com.service.hotel.booking.entity.UserEntity;
import com.service.hotel.booking.keycloak.KeycloakCreateUserRequestDto;
import com.service.hotel.booking.keycloak.KeycloakPasswordCredentials;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserEntity convert(CreateUserDto createUserDto, String keycloakId) {
        if (keycloakId != null) {
            return new UserEntity(
                    UUID.fromString(keycloakId),
                    createUserDto.getFirstName(),
                    createUserDto.getLastName(),
                    createUserDto.getEmail(),
                    createUserDto.getUsername(),
                    createUserDto.getDateOfBirth()
            );
        }
        return new UserEntity(
                createUserDto.getFirstName(),
                createUserDto.getLastName(),
                createUserDto.getEmail(),
                createUserDto.getUsername(),
                createUserDto.getDateOfBirth()
        );
    }

    public KeycloakCreateUserRequestDto convertKeycloak(CreateUserDto createUserDto) {
        return new KeycloakCreateUserRequestDto(
                createUserDto.getFirstName(),
                createUserDto.getLastName(),
                createUserDto.getEmail(),
                createUserDto.getUsername(),
                true,
                convertPassword(createUserDto.getPassword())
        );
    }

    private List<KeycloakPasswordCredentials> convertPassword(String password) {
        List<KeycloakPasswordCredentials> passwordCredentials = new ArrayList<>();
        KeycloakPasswordCredentials passwordCredential = new KeycloakPasswordCredentials(
                "password", password, false
        );
        passwordCredentials.add(passwordCredential);
        return passwordCredentials;
    }
}
