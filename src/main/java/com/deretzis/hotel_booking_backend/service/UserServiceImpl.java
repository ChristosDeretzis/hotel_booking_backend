package com.deretzis.hotel_booking_backend.service;

import com.deretzis.hotel_booking_backend.adapter.KeycloakAdapter;
import com.deretzis.hotel_booking_backend.dto.CreateUserDto;
import com.deretzis.hotel_booking_backend.dto.ServiceAccessTokenResponseDto;
import com.deretzis.hotel_booking_backend.mapper.UserMapper;
import com.deretzis.hotel_booking_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void createNewUser(CreateUserDto createUserDto) {
        // check if there is a user with the same username and/or password in db and keycloak
        // if there is a user throw an exception
        // else insert the user in the keycloak
        // if the keycloak insertion throws an error, then exit
        // else insert the user in the db

        String encodedPassword = passwordEncoder.encode(createUserDto.getPassword());
        createUserDto.setPassword(encodedPassword);
        userRepository.save(userMapper.convert(createUserDto));
    }
}
