package com.service.hotel.booking.controller;

import com.service.hotel.booking.dto.CreateUserDto;
import com.service.hotel.booking.error.AuthErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@RequestMapping("/users")
public interface UserController {

    @Operation(summary = "create a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "user created successfully",
                content = { @Content}),
            @ApiResponse(responseCode = "400", description = "bad request",
                content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = AuthErrorMessage.class))}),
            @ApiResponse(responseCode = "401", description = "unauthorized user",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthErrorMessage.class))}),
            @ApiResponse(responseCode = "409", description = "user with same email or username already exists",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthErrorMessage.class))})
    })
    @PostMapping("/signup")
    void createNewUser(@RequestBody CreateUserDto createUserDto);
}
