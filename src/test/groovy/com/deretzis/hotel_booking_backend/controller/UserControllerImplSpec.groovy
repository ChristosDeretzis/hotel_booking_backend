package com.deretzis.hotel_booking_backend.controller

import com.deretzis.hotel_booking_backend.configuration.SecurityConfiguration
import com.deretzis.hotel_booking_backend.dto.CreateUserDto
import com.deretzis.hotel_booking_backend.service.UserService
import com.deretzis.hotel_booking_backend.service.UserServiceImpl
import com.fasterxml.jackson.databind.ObjectMapper
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultMatcher
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders
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
                    date_of_birth: LocalDate.parse("24/01/2000", DateTimeFormatter.ofPattern("dd/MM/yyyy"))
            )

            Map<String, String> requestBody = new HashMap<>()
            requestBody.put("firstName", "Christos")
            requestBody.put("lastName", "Deretzis")
            requestBody.put("email", "someone@email.com")
            requestBody.put("password", "user1234")
            requestBody.put("username", "silPow49")
            requestBody.put("date_of_birth", "24/01/2000")

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
                assert createUserDto.date_of_birth == newUser.getDate_of_birth()
            }
    }
}
