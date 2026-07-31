package com.cinema.backend.controller;

import com.cinema.backend.dto.request.AuthRequest;
import com.cinema.backend.dto.request.RegisterRequest;
import com.cinema.backend.dto.response.AuthResponse;
import com.cinema.backend.dto.response.UserResponse;
import com.cinema.backend.entity.User;
import com.cinema.backend.security.JwtService;
import com.cinema.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;

    public AuthController(
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            UserService userService) {

        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        UserDetails userDetails =
                (UserDetails) authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getUsername(),
                                request.getPassword()
                        )
                ).getPrincipal();

        String jwt = jwtService.generateToken(userDetails);

        return new AuthResponse(jwt);

    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request) {

        User newUser = userService.register(request);
        UserResponse response = userService.convertToResponse(newUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}