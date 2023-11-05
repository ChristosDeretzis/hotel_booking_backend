package com.service.hotel.booking.unit.controller

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.service.hotel.booking.configuration.SecurityConfiguration
import com.service.hotel.booking.controller.UserControllerImpl
import com.service.hotel.booking.dto.CreateUserDto
import com.service.hotel.booking.service.UserServiceImpl
import com.fasterxml.jackson.databind.ObjectMapper
import com.service.hotel.booking.utils.creators.UserCreator
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
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
        given:
            CreateUserDto newUser = UserCreator.randomUserRequestDto()

        when:
            mockMvc.perform(MockMvcRequestBuilders.post("/users/signup")
                .content((new ObjectMapper().registerModule(new JavaTimeModule())).writeValueAsString(newUser))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())

        then:
            1 * userService.createNewUser(newUser)
            0 * _
    }
}
