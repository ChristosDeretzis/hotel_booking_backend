package com.deretzis.hotel_booking_backend.unit.mapper

import com.deretzis.hotel_booking_backend.dto.CreateUserDto
import com.deretzis.hotel_booking_backend.entity.UserEntity
import com.deretzis.hotel_booking_backend.keycloak.KeycloakCreateUserRequestDto
import com.deretzis.hotel_booking_backend.mapper.UserMapper
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
                 date_of_birth: LocalDate.now().minusYears(18).minusDays(1)
            )
            def keyCloakUserId = UUID.randomUUID().toString()

        when:
            UserEntity response = userMapper.convert(userDto, keyCloakUserId)

        then:
            response.id == UUID.fromString(keyCloakUserId)
            response.firstName == userDto.getFirstName()
            response.lastName == userDto.getLastName()
            response.email == userDto.getEmail()
            response.username == userDto.getUsername()
            response.dob == userDto.getDate_of_birth()
    }

    def "Convert CreateUserRequest to User Entity with null keycloak id" () {
        given:
            def userDto = new CreateUserDto(
                    firstName: "Christos",
                    lastName: "Deretzis",
                    email: "chris@example.com",
                    password: "user1234",
                    username: "chris345",
                    date_of_birth: LocalDate.now().minusYears(18).minusDays(1)
            )
            def keyCloakUserId = null

        when:
            UserEntity response = userMapper.convert(userDto, keyCloakUserId)

        then:
            response.firstName == userDto.getFirstName()
            response.lastName == userDto.getLastName()
            response.email == userDto.getEmail()
            response.username == userDto.getUsername()
            response.dob == userDto.getDate_of_birth()
    }

    def "Convert CreateUserRequest to KeycloakCreateUserDto" () {
        given:
            def userDto = new CreateUserDto(
                    firstName: "Christos",
                    lastName: "Deretzis",
                    email: "chris@example.com",
                    password: "user1234",
                    username: "chris345",
                    date_of_birth: LocalDate.now().minusYears(18).minusDays(1)
            )
            def keyCloakUserId = null

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
