package com.bookhub.security.dto;

public record LoginResponse(
        String token,
        String refreshToken,
        String username,
        String type
) {
    /** Constructeur à 3 arguments → type = "Bearer" par défaut (RFC 6750). */
    public LoginResponse(String token, String refreshToken, String username) {
        this(token, refreshToken, username, "Bearer");
    }
}