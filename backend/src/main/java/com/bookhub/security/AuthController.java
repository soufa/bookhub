package com.bookhub.security;

import com.bookhub.security.dto.LoginRequest;
import com.bookhub.security.dto.LoginResponse;
import com.bookhub.security.dto.RefreshRequest;
import com.bookhub.security.dto.RefreshResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    public AuthController(AuthenticationManager authManager,
                          JwtService jwtService,
                          RefreshTokenService refreshTokenService) {
        this.authManager = authManager;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        UserDetails user = (UserDetails) auth.getPrincipal();
        String accessToken  = jwtService.generateToken(user);
        String refreshToken = refreshTokenService.createRefreshToken(user.getUsername());

        return new LoginResponse(accessToken, refreshToken, user.getUsername());
    }

    @PostMapping("/refresh")
    public RefreshResponse refresh(@Valid @RequestBody RefreshRequest request) {
        RefreshTokenService.TokenPair pair = refreshTokenService.rotate(request.refreshToken());
        return new RefreshResponse(pair.accessToken(), pair.refreshToken());
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(@Valid @RequestBody RefreshRequest request) {
        refreshTokenService.revoke(request.refreshToken());
    }

    @ExceptionHandler(InvalidRefreshTokenException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public java.util.Map<String, Object> handleInvalidRefresh(InvalidRefreshTokenException ex) {
        return java.util.Map.of(
                "status", 401,
                "error", "Unauthorized",
                "message", ex.getMessage()
        );
    }
}