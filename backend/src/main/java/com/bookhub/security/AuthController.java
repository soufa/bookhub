package com.bookhub.security;

import com.bookhub.security.dto.LoginRequest;
import com.bookhub.security.dto.LoginResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authManager;
    private final AppUserDetailsService userDetailsService;
    private final JwtService jwtService;

    public AuthController(AuthenticationManager authManager,
                          AppUserDetailsService userDetailsService,
                          JwtService jwtService) {
        this.authManager = authManager;
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest req) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.username(), req.password()));

        UserDetails user = userDetailsService.loadUserByUsername(req.username());
        String token = jwtService.generateToken(user);

        return ResponseEntity.ok(new LoginResponse(token, user.getUsername()));
    }
}