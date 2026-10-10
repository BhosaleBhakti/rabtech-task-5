package com.rabtech.dto;

public record AuthResponse(
        String token,
        String tokenType,
        String message
) {
}