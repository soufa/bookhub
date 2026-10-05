package com.bookhub.security;

import com.bookhub.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class JwtEndToEndIT extends AbstractIntegrationTest {

    @Autowired private MockMvc mockMvc;

    @Test
    void should_complete_full_login_then_access_protected_endpoint() throws Exception {
        // 1. Login → récupère le token
        String loginBody = """
            {"username":"admin","password":"admin123"}
            """;

        MvcResult loginResult = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.username").value("admin"))
                .andReturn();

        String responseBody = loginResult.getResponse().getContentAsString();
        String token = responseBody.substring(
                responseBody.indexOf("\"token\":\"") + 9,
                responseBody.indexOf("\"", responseBody.indexOf("\"token\":\"") + 9)
        );

        // 2. Appel protégé avec le token → 200
        mockMvc.perform(get("/api/books")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    void should_reject_protected_endpoint_without_token() throws Exception {
        mockMvc.perform(get("/api/books"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Unauthorized"));
    }

    @Test
    void should_reject_protected_endpoint_with_invalid_token() throws Exception {
        mockMvc.perform(get("/api/books")
                        .header("Authorization", "Bearer invalid.jwt.token"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void should_forbid_user_from_deleting_book() throws Exception {
        // Login en tant que user simple
        String loginBody = """
            {"username":"user","password":"user123"}
            """;

        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginBody))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        String token = response.substring(
                response.indexOf("\"token\":\"") + 9,
                response.indexOf("\"", response.indexOf("\"token\":\"") + 9)
        );

        // Tentative de DELETE → 403
        mockMvc.perform(delete("/api/books/999")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.error").value("Forbidden"));
    }
}