package com.service.hotel.booking.unit.controller

import com.service.hotel.booking.configuration.SecurityConfiguration
import com.service.hotel.booking.controller.UserControllerImpl
import com.service.hotel.booking.dto.CreateUserDto
import com.service.hotel.booking.service.UserServiceImpl
import com.fasterxml.jackson.databind.ObjectMapper
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import spock.lang.Specification

import java.time.LocalDate
import java.time.format.DateTimeFormatter

@WebMvcTest
@ContextConfiguration(classes = [UserControllerImpl, SecurityConfiguration])
class UserControllerImplSpec extends Specification {

    @Autowired
    MockMvc mockMvc

    @SpringBean
    private final UserServiceImpl userService = Mock()

    def "controller create user endpoint successful"() {
            CreateUserDto newUser = new CreateUserDto(
                    firstName: "Christos",
                    lastName: "Deretzis",
                    email: "someone@email.com",
                    password: "user1234",
                    username: "silPow49",
                    dateOfBirth: LocalDate.parse("24/01/2000", DateTimeFormatter.ofPattern("dd/MM/yyyy"))
            )

            Map<String, String> requestBody = [:]
            requestBody.with {
                put("firstName", "Christos")
                put("lastName", "Deretzis")
                put("email", "someone@email.com")
                put("username", "silPow49")
                put("password", "user1234")
                put("dateOfBirth", "24/01/2000")
            }

        when:
            mockMvc.perform(MockMvcRequestBuilders.post("/users/signup")
                .content((new ObjectMapper()).writeValueAsString(requestBody))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
        then:
            1 * userService.createNewUser(_) >> { CreateUserDto createUserDto ->
                assert createUserDto.firstName == newUser.getFirstName()
                assert createUserDto.lastName == newUser.getLastName()
                assert createUserDto.email == newUser.getEmail()
                assert createUserDto.username == newUser.getUsername()
                assert createUserDto.password == newUser.getPassword()
                assert createUserDto.dateOfBirth == newUser.getDateOfBirth()
            }
    }
}
