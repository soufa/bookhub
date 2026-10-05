package com.bookhub.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Map;

@Service
public class AppUserDetailsService implements UserDetailsService {

    private final Map<String, UserDetails> users;
    private final PasswordEncoder passwordEncoder;

    public AppUserDetailsService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        this.users = Map.of(
                "user",  buildUser("user",  "user123",  "USER"),
                "admin", buildUser("admin", "admin123", "USER", "ADMIN")
        );
    }

    private UserDetails buildUser(String username, String rawPassword, String... roles) {
        String[] cleanRoles = Arrays.stream(roles)
                .map(r -> r.startsWith("ROLE_") ? r.substring(5) : r)
                .filter(r -> !r.isBlank())
                .toArray(String[]::new);

        return User.withUsername(username)
                .password(passwordEncoder.encode(rawPassword))
                .roles(cleanRoles)
                .build();
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        UserDetails user = users.get(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }
        return user;
    }
}