package com.service.hotel.booking.unit.mapper

import com.service.hotel.booking.dto.CreateUserDto
import com.service.hotel.booking.entity.UserEntity
import com.service.hotel.booking.keycloak.KeycloakCreateUserRequestDto
import com.service.hotel.booking.mapper.UserMapper
import spock.lang.Specification

import java.time.LocalDate

class UserMapperSpec extends Specification {

    UserMapper userMapper

    def setup() {
        userMapper = new UserMapper()
    }

    def "Convert CreateUserRequest to User Entity with non null keycloak id" () {
        given:
            def userDto = new CreateUserDto(
                 firstName: "Christos",
                 lastName: "Deretzis",
                 email: "chris@example.com",
                 password: "user1234",
                 username: "chris345",
                 dateOfBirth: LocalDate.now().minusYears(18).minusDays(1)
            )
            def keyCloakUserId = UUID.randomUUID().toString()

        when:
            UserEntity response = userMapper.convert(userDto, keyCloakUserId)

        then:
            with(response) {
                id == UUID.fromString(keyCloakUserId)
                firstName == userDto.getFirstName()
                lastName == userDto.getLastName()
                email == userDto.getEmail()
                username == userDto.getUsername()
                dob == userDto.getDateOfBirth()
            }
    }

    def "Convert CreateUserRequest to User Entity with null keycloak id" () {
        given:
            def userDto = new CreateUserDto(
                    firstName: "Christos",
                    lastName: "Deretzis",
                    email: "chris@example.com",
                    password: "user1234",
                    username: "chris345",
                    dateOfBirth: LocalDate.now().minusYears(18).minusDays(1)
            )

        when:
            UserEntity response = userMapper.convert(userDto, null)

        then:
            with(response) {
                firstName == userDto.getFirstName()
                lastName == userDto.getLastName()
                email == userDto.getEmail()
                username == userDto.getUsername()
                dob == userDto.getDateOfBirth()
            }
    }

    def "Convert CreateUserRequest to KeycloakCreateUserDto" () {
        given:
            def userDto = new CreateUserDto(
                    firstName: "Christos",
                    lastName: "Deretzis",
                    email: "chris@example.com",
                    password: "user1234",
                    username: "chris345",
                    dateOfBirth: LocalDate.now().minusYears(18).minusDays(1)
            )

        when:
            KeycloakCreateUserRequestDto response = userMapper.convertKeycloak(userDto)

        then:
            response.firstName == userDto.getFirstName()
            response.lastName == userDto.getLastName()
            response.email == userDto.getEmail()
            response.username == userDto.getUsername()
            response.enabled == true
            response.getCredentials().size() == 1
            verifyAll(response.credentials[0]) {
                type == "password"
                value == userDto.getPassword()
                temporary == false
            }
    }
}
