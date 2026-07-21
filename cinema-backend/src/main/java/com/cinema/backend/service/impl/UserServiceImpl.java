package com.cinema.backend.service.impl;

import com.cinema.backend.dto.response.UserResponse;
import com.cinema.backend.entity.User;
import com.cinema.backend.repository.UserRepository;
import com.cinema.backend.service.UserService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public UserResponse convertToResponse(User user) {

        UserResponse response = new UserResponse();

        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setPhone(user.getPhone());
        response.setFullname(user.getFullname());
        response.setAvatar(user.getAvatar());
        response.setStatus(user.getStatus());
        response.setRole(user.getRole().getName());

        return response;
    }
}