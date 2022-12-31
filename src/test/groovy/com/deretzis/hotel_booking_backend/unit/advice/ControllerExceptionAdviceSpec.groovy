package com.deretzis.hotel_booking_backend.unit.advice

import com.deretzis.hotel_booking_backend.advice.ControllerExceptionAdvice
import com.deretzis.hotel_booking_backend.error.AuthErrorMessage
import com.deretzis.hotel_booking_backend.exception.GenericKeycloakException
import com.deretzis.hotel_booking_backend.exception.RequestKeycloakException
import com.deretzis.hotel_booking_backend.exception.UnauthorizedKeycloakException
import com.deretzis.hotel_booking_backend.exception.UserExistsKeycloakException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.context.request.WebRequest
import spock.lang.Specification

class ControllerExceptionAdviceSpec extends Specification {

    ControllerExceptionAdvice controllerExceptionAdvice
    WebRequest webRequest
    AuthErrorMessage authErrorMessage = new AuthErrorMessage(100, "dummy message")

    def setup() {
        controllerExceptionAdvice = new ControllerExceptionAdvice()
        webRequest = Mock()
    }

    def "HandleGenericKeycloakException"() {
        given:
            GenericKeycloakException genericKeycloakException = new GenericKeycloakException(authErrorMessage)

        when:
            ResponseEntity<Object> response = controllerExceptionAdvice.handleGenericKeycloakException(genericKeycloakException, webRequest)

        then:
            response.getStatusCode() == HttpStatus.BAD_REQUEST
            response.getBody() == authErrorMessage
    }

    def "HandleRequestKeycloakException"() {
        given:
            RequestKeycloakException requestKeycloakException = new RequestKeycloakException(authErrorMessage)

        when:
            ResponseEntity<Object> response = controllerExceptionAdvice.handleRequestKeycloakException(requestKeycloakException, webRequest)

        then:
            response.getStatusCode() == HttpStatus.BAD_REQUEST
            response.getBody() == authErrorMessage
    }

    def "TestHandleUnauthorizedKeycloakException"() {
        given:
            UnauthorizedKeycloakException unauthorizedKeycloakException = new UnauthorizedKeycloakException(authErrorMessage)

        when:
            ResponseEntity<Object> response = controllerExceptionAdvice.handleUnauthorizedKeycloakException(unauthorizedKeycloakException, webRequest)

        then:
            response.getStatusCode() == HttpStatus.UNAUTHORIZED
            response.getBody() == authErrorMessage
    }

    def "TestHandleUserExistsKeycloakException"() {
        given:
        UserExistsKeycloakException userExistsKeycloakException = new UserExistsKeycloakException(authErrorMessage)

        when:
            ResponseEntity<Object> response = controllerExceptionAdvice.handleUserExistsKeycloakException(userExistsKeycloakException, webRequest)

        then:
            response.getStatusCode() == HttpStatus.CONFLICT
            response.getBody() == authErrorMessage
    }
}
