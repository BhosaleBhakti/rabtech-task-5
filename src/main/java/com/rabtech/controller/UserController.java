package com.rabtech.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @GetMapping("/profile")
    public Map<String, String> getProfile(Authentication authentication) {
        return Map.of(
                "email", authentication.getName(),
                "message", "You are authenticated successfully"
        );
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, String> adminOnly() {
        return Map.of("message", "Welcome, Admin!");
    }
}