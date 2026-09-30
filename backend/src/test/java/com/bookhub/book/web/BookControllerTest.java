package com.bookhub.book.web;

import com.bookhub.book.BookDto;
import com.bookhub.book.BookService;
import com.bookhub.book.BookStatus;
import com.bookhub.shared.domain.Isbn;
import com.bookhub.shared.domain.Money;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Erreur — Spring Security actif dans les tests @WebMvcTest
 * Les erreurs 401 et 403 viennent de Spring Security. Vous l'avez exclu dans BookHubApplication pour l'app, mais @WebMvcTest charge un contexte séparé qui active la sécurité par défaut.
 *
 * Résultat : vos endpoints sont protégés dans les tests → 401 (non authentifié) ou 403 (interdit).
 *
 * Solution — Désactiver les filtres de sécurité dans les tests
 * Une seule ligne à ajouter dans BookControllerTest.java :
 *
 * java
 * @WebMvcTest(BookController.class)
 * @AutoConfigureMockMvc(addFilters = false)   // ← AJOUTER CETTE LIGNE
 * class BookControllerTest {
 */
@WebMvcTest(com.bookhub.book.web.BookController.class)
@AutoConfigureMockMvc(addFilters = false)   // ← AJOUTER CETTE LIGNE

class BookControllerTest {
    /**
     * @WebMvcTest : charge uniquement la couche web (pas de JPA, pas de BDD)
     *
     * @MockBean : remplace BookService par un mock Mockito
     *
     * MockMvc : simule des requêtes HTTP sans vrai serveur
     *
     * jsonPath : vérifie le JSON de la réponse
     */
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookService service;

    private final BookDto sample = new BookDto(1L, "Effective Java", "Joshua Bloch",
            new Isbn("9780134685991"), Money.of("45.00", "EUR"), BookStatus.AVAILABLE);

    @Test
    void should_return_all_books() throws Exception {
        when(service.findAll()).thenReturn(List.of(sample));

        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Effective Java"))
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void should_return_book_by_id() throws Exception {
        when(service.findById(1L)).thenReturn(Optional.of(sample));

        mockMvc.perform(get("/api/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Effective Java"));
    }

    @Test
    void should_return_404_when_book_not_found() throws Exception {
        when(service.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/books/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void should_create_book() throws Exception {
        BookDto noId = new BookDto(null, "Effective Java", "Joshua Bloch",
                new Isbn("9780134685991"), Money.of("45.00", "EUR"), BookStatus.AVAILABLE);
        when(service.create(any())).thenReturn(sample);

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(noId)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void should_delete_book() throws Exception {
        when(service.delete(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/books/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void should_return_404_when_deleting_unknown() throws Exception {
        when(service.delete(999L)).thenReturn(false);

        mockMvc.perform(delete("/api/books/999"))
                .andExpect(status().isNotFound());
    }
}