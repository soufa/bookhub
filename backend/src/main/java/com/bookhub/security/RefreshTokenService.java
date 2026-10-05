package com.bookhub.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository repository;
    private final AppUserDetailsService userDetailsService;
    private final JwtService jwtService;

    @Value("${app.jwt.refresh-expiration-days:7}")
    private long refreshExpirationDays;

    public RefreshTokenService(RefreshTokenRepository repository,
                               AppUserDetailsService userDetailsService,
                               JwtService jwtService) {
        this.repository = repository;
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
    }

    @Transactional
    public String createRefreshToken(String username) {
        String token = UUID.randomUUID().toString();
        Instant expiresAt = Instant.now().plus(refreshExpirationDays, ChronoUnit.DAYS);

        repository.save(new RefreshToken(token, username, expiresAt));
        return token;
    }

    /**
     * Valide un refresh token, le révoque (rotation), et renvoie un nouveau couple.
     */
    @Transactional
    public TokenPair rotate(String oldToken) {
        RefreshToken stored = repository.findByToken(oldToken)
                .orElseThrow(() -> new InvalidRefreshTokenException("Refresh token inconnu"));

        if (stored.isRevoked()) {
            // Sécurité : un token révoqué réutilisé = possible vol
            repository.revokeAllForUser(stored.getUsername());
            throw new InvalidRefreshTokenException("Refresh token révoqué — session compromise");
        }

        if (stored.isExpired()) {
            throw new InvalidRefreshTokenException("Refresh token expiré");
        }

        // Rotation : révoque l'ancien
        stored.setRevoked(true);

        UserDetails user;
        try {
            user = userDetailsService.loadUserByUsername(stored.getUsername());
        } catch (UsernameNotFoundException e) {
            throw new InvalidRefreshTokenException("Utilisateur introuvable");
        }

        String newAccess  = jwtService.generateToken(user);
        String newRefresh = createRefreshToken(stored.getUsername());

        return new TokenPair(newAccess, newRefresh);
    }

    @Transactional
    public void revoke(String token) {
        repository.findByToken(token)
                .ifPresent(t -> t.setRevoked(true));
    }

    public record TokenPair(String accessToken, String refreshToken) {}
}