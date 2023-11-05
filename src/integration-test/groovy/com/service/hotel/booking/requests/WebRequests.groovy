package com.service.hotel.booking.requests

import com.service.hotel.booking.dto.CreateUserDto
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.test.web.reactive.server.WebTestClient

@Component
class WebRequests {

    WebTestClient webTestClient

    def setWebTestClient(WebTestClient webTestClient) {
        this.webTestClient = webTestClient
    }

    def signupUser(CreateUserDto createUserDto) {
        webTestClient.post()
                .uri('/users/signup')
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(createUserDto)
                .exchange()
    }
}
