package com.bookhub.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.lang.reflect.Field;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private static final String TEST_SECRET =
            "0123456789abcdef0123456789abcdef0123456789abcdef0123456789abcdef";

    private JwtService jwtService;

    @BeforeEach
    void setUp() throws Exception {
        jwtService = new JwtService();
        setField("secretKey", TEST_SECRET);       // ← secretKey (pas secret)
        setField("expirationMs", 3_600_000L);
    }

    private void setField(String name, Object value) throws Exception {
        Field f = JwtService.class.getDeclaredField(name);
        f.setAccessible(true);
        f.set(jwtService, value);
    }

    @Test
    void should_generate_token_with_three_parts() {
        UserDetails user = User.withUsername("alice").password("x").roles("USER").build();
        String token = jwtService.generateToken(user);

        assertNotNull(token);
        assertEquals(3, token.split("\\.").length,
                "JWT = 3 parties séparées par des points");
    }

    @Test
    void should_extract_username_from_token() {
        UserDetails user = User.withUsername("bob").password("x").roles("USER").build();
        String token = jwtService.generateToken(user);

        assertEquals("bob", jwtService.extractUsername(token));
    }

    @Test
    void should_validate_token_for_same_user() {
        UserDetails user = User.withUsername("carol").password("x").roles("USER").build();
        String token = jwtService.generateToken(user);

        assertTrue(jwtService.isTokenValid(token, user));
    }

    @Test
    void should_reject_token_for_different_user() {
        UserDetails alice = User.withUsername("alice").password("x").roles("USER").build();
        UserDetails bob   = User.withUsername("bob").password("x").roles("USER").build();

        String token = jwtService.generateToken(alice);

        assertFalse(jwtService.isTokenValid(token, bob));
    }

    @Test
    void should_produce_different_tokens_for_different_users() {
        UserDetails alice = User.withUsername("alice").password("x").roles("USER").build();
        UserDetails bob   = User.withUsername("bob").password("x").roles("USER").build();

        assertNotEquals(jwtService.generateToken(alice),
                jwtService.generateToken(bob));
    }

    @Test
    void should_throw_when_parsing_expired_token() throws Exception {
        setField("expirationMs", -1000L);   // token déjà expiré

        UserDetails user = User.withUsername("dave").password("x").roles("USER").build();
        String expiredToken = jwtService.generateToken(user);

        Exception ex = assertThrows(Exception.class,
                () -> jwtService.extractUsername(expiredToken));

        assertTrue(ex.getMessage().toLowerCase().contains("expired"),
                "Message doit mentionner l'expiration : " + ex.getMessage());
    }

    @Test
    void should_sign_token_with_hmac_algo_depending_on_key_size() {
        UserDetails user = User.withUsername("eve").password("x").roles("USER").build();
        String token = jwtService.generateToken(user);

        String header = new String(Base64.getUrlDecoder()
                .decode(token.split("\\.")[0]));

        // Clé de 64 octets → jjwt choisit HS512 automatiquement
        assertTrue(header.contains("HS512"),
                "Header doit contenir HS512 (clé 64 octets) : " + header);
    }
}