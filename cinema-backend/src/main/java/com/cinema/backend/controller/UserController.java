package com.cinema.backend.controller;

import com.cinema.backend.dto.response.UserResponse;
import com.cinema.backend.entity.User;
import com.cinema.backend.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public UserResponse getCurrentUser(Authentication authentication) {

        String username = authentication.getName();

        User user = userService.findByUsername(username)
                .orElseThrow();

        return userService.convertToResponse(user);
    }

}