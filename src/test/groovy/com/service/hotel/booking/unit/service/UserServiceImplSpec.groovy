package com.service.hotel.booking.unit.service

import com.service.hotel.booking.dto.CreateUserDto
import com.service.hotel.booking.entity.UserEntity
import com.service.hotel.booking.keycloak.KeycloakCreateUserRequestDto
import com.service.hotel.booking.keycloak.KeycloakPasswordCredentials
import com.service.hotel.booking.mapper.UserMapper
import com.service.hotel.booking.repository.UserRepository
import com.service.hotel.booking.service.UserService
import com.service.hotel.booking.service.UserServiceImpl
import com.service.hotel.booking.adapter.KeycloakAdapter
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
                    dateOfBirth: LocalDate.now().minusYears(18).minusDays(1)
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
