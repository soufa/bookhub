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

    private final PasswordEncoder passwordEncoder;

    // Map username → [password, roles]
    private static final Map<String, String[]> USERS = Map.of(
            "user",  new String[]{"user123", "USER"},
            "admin", new String[]{"admin123", "USER", "ADMIN"}
    );

    public AppUserDetailsService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        String[] entry = USERS.get(username);
        if (entry == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }

        String rawPassword = entry[0];
        String[] roles = Arrays.copyOfRange(entry, 1, entry.length);

        String[] cleanRoles = Arrays.stream(roles)
                .map(r -> r.startsWith("ROLE_") ? r.substring(5) : r)
                .filter(r -> !r.isBlank())
                .toArray(String[]::new);

        return User.withUsername(username)
                .password(passwordEncoder.encode(rawPassword))
                .roles(cleanRoles)
                .build();
    }
}