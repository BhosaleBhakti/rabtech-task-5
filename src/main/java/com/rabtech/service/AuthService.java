package com.rabtech.service;

import com.rabtech.dto.AuthResponse;
import com.rabtech.dto.LoginRequest;
import com.rabtech.dto.RegisterRequest;
import com.rabtech.entity.User;
import com.rabtech.repository.UserRepository;
import com.rabtech.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email is already registered");
        }

        User user = new User();
        user.setName(request.name());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole("USER");

        User savedUser = userRepository.save(user);

        String token = jwtService.generateToken(
                savedUser.getEmail(), savedUser.getRole());

        return new AuthResponse(token, "Bearer", "Registration successful");
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(), request.password()));

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() ->
                        new IllegalArgumentException("User not found"));

        String token = jwtService.generateToken(
                user.getEmail(), user.getRole());

        return new AuthResponse(token, "Bearer", "Login successful");
    }
}
