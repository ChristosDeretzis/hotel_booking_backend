package com.service.hotel.booking.functional.tests

import com.service.hotel.booking.dto.CreateUserDto
import com.service.hotel.booking.error.AuthErrorCodes
import com.service.hotel.booking.error.AuthErrorMessage
import com.service.hotel.booking.functional.BaseFunctionalSpec
import org.springframework.http.HttpStatus
import org.springframework.test.web.reactive.server.WebTestClient

import java.time.LocalDate

class UserFunctionalSpec extends BaseFunctionalSpec {

    def "create user"() {
        given: 'Given a new user that is registered'
        CreateUserDto createUserDto = new CreateUserDto(
                firstName: 'Nick',
                lastName: 'Ioannou',
                email: 'nick01@email.com',
                password: 'user1234',
                username: 'nick_pap_01',
                dateOfBirth: LocalDate.now().minusYears(18).minusDays(1)
        )

        when: 'New user is saved in keycloak and db'
            WebTestClient.ResponseSpec responseSpec = webRequests.signupUser(createUserDto)

        then: 'Status is 200'
            responseSpec.expectStatus().isOk()

        and: 'Try to create the same user again'
            WebTestClient.ResponseSpec responseSpec02 = webRequests.signupUser(createUserDto)

        then: 'Assert the error code is 409 (Conflict)'
            responseSpec02.expectStatus().isEqualTo(HttpStatus.CONFLICT)

        and: 'assert the error body (user already exists)'
        AuthErrorMessage alreadyExistsMessage = responseSpec02.expectBody(AuthErrorMessage).returnResult().responseBody
            assert alreadyExistsMessage.code == AuthErrorCodes.USER_EXISTS_ERROR.code
            assert alreadyExistsMessage.message == AuthErrorCodes.USER_EXISTS_ERROR.message
    }
}
