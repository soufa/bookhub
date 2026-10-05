package com.bookhub.security.dto;

public record RefreshResponse(
        String accessToken,
        String refreshToken
) {}