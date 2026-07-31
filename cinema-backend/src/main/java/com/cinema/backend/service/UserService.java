package com.cinema.backend.service;

import com.cinema.backend.dto.response.UserResponse;
import com.cinema.backend.entity.User;
import com.cinema.backend.dto.request.RegisterRequest;
import java.util.Optional;

public interface UserService {

    Optional<User> findByUsername(String username);

    UserResponse convertToResponse(User user);
    User register(RegisterRequest request);
}