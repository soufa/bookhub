package com.bookhub.book.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class BookControllerSecurityTest {

    @Autowired private MockMvc mockMvc;

    @Test
    void should_reject_anonymous_access() throws Exception {
        mockMvc.perform(get("/api/books"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void should_allow_user_to_list() throws Exception {
        mockMvc.perform(get("/api/books")).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void should_forbid_user_to_delete() throws Exception {
        mockMvc.perform(delete("/api/books/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void should_allow_admin_to_delete() throws Exception {
        // 404 = autorisé mais ressource absente (le contrôle d'accès est passé)
        mockMvc.perform(delete("/api/books/9999"))
                .andExpect(status().isNotFound());
    }
}