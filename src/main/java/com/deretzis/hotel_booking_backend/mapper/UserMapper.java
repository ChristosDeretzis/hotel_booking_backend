package com.deretzis.hotel_booking_backend.mapper;

import com.deretzis.hotel_booking_backend.dto.CreateUserDto;
import com.deretzis.hotel_booking_backend.keycloak.KeycloakCreateUserRequestDto;
import com.deretzis.hotel_booking_backend.keycloak.KeycloakPasswordCredentials;
import com.deretzis.hotel_booking_backend.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
                    createUserDto.getDate_of_birth()
            );
        }
        return new UserEntity(
                createUserDto.getFirstName(),
                createUserDto.getLastName(),
                createUserDto.getEmail(),
                createUserDto.getUsername(),
                createUserDto.getDate_of_birth()
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
