package com.bookhub.security.dto;

public record LoginResponse(String token, String username, String type) {
    public LoginResponse(String token, String username) {
        this(token, username, "Bearer");
    }
}