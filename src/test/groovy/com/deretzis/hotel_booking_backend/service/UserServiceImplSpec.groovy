package com.deretzis.hotel_booking_backend.service

import com.deretzis.hotel_booking_backend.adapter.KeycloakAdapter
import com.deretzis.hotel_booking_backend.dto.CreateUserDto
import com.deretzis.hotel_booking_backend.entity.UserEntity
import com.deretzis.hotel_booking_backend.keycloak.KeycloakCreateUserRequestDto
import com.deretzis.hotel_booking_backend.keycloak.KeycloakPasswordCredentials
import com.deretzis.hotel_booking_backend.mapper.UserMapper
import com.deretzis.hotel_booking_backend.repository.UserRepository
import spock.lang.Specification

import java.time.LocalDate

class UserServiceImplSpec extends Specification {

    UserRepository userRepository
    UserMapper userMapper
    KeycloakAdapter keycloakAdapter
    UserService userService

    def setup() {
        userRepository = Mock(UserRepository)
        userMapper = Mock(UserMapper)
        keycloakAdapter = Mock(KeycloakAdapter)
        userService = new UserServiceImpl(userRepository, userMapper, keycloakAdapter)
    }

    def "create user successfully" () {
        given:
            def userDto = new CreateUserDto(
                firstName: "Christos",
                lastName: "Deretzis",
                email: "chris@example.com",
                password: "user1234",
                username: "chris345",
                date_of_birth: LocalDate.now().minusYears(18).minusDays(1)
            )

            def keycloakUserDto = new KeycloakCreateUserRequestDto(
                    firstName: "Christos",
                    lastName: "Deretzis",
                    email: "chris@example.com",
                    username: "chris345",
                    enabled: true,
                    credentials: List.of(new KeycloakPasswordCredentials(
                                                type: "password",
                                                value: "user1234",
                                                temporary: false
                                        ))
            )

            def keycloakUserId = UUID.randomUUID().toString()

            def userEntity = new UserEntity(
                    id: UUID.fromString(keycloakUserId),
                    firstName: "Christos",
                    lastName: "Deretzis",
                    email: "chris@example.com",
                    username: "chris345",
                    dob: LocalDate.now().minusYears(18).minusDays(1)
            )


        when:
            userService.createNewUser(userDto)

        then:
            1 * userMapper.convertKeycloak(userDto) >> keycloakUserDto
            1 * keycloakAdapter.createKeycloakUser(keycloakUserDto) >> keycloakUserId
            1 * userMapper.convert(userDto, keycloakUserId) >> userEntity
            1 * userRepository.save(userEntity)
            0 * _

    }

}
