package com.service.hotel.booking.service;

import com.service.hotel.booking.adapter.KeycloakAdapter;
import com.service.hotel.booking.dto.CreateUserDto;
import com.service.hotel.booking.mapper.UserMapper;
import com.service.hotel.booking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private final KeycloakAdapter keycloakAdapter;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, KeycloakAdapter keycloakAdapter) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.keycloakAdapter = keycloakAdapter;
    }

    @Override
    public void createNewUser(CreateUserDto createUserDto) {
        // check if there is a user with the same username and/or password in db and keycloak
        // if there is a user throw an exception
        // else insert the user in the keycloak
        // if the keycloak insertion throws an error, then exit
        // else insert the user in the db;

        String keycloakId = keycloakAdapter.createKeycloakUser(userMapper.convertKeycloak(createUserDto));
        userRepository.save(userMapper.convert(createUserDto, keycloakId));
    }
}
