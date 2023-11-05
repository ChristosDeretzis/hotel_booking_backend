package com.service.hotel.booking.utils.creators

import com.service.hotel.booking.dto.CreateUserDto

import java.time.LocalDate
import java.time.format.DateTimeFormatter

class UserCreator {

    static randomUserRequestDto() {
        return new CreateUserDto(
                firstName: "Christos",
                lastName: "Deretzis",
                email: "someone@email.com",
                password: "user1234",
                username: "silPow49",
                dateOfBirth: LocalDate.parse("24/01/2000", DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        )
    }
}
