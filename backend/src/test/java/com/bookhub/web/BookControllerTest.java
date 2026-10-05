package com.bookhub.web;

import com.bookhub.book.BookDto;
import com.bookhub.book.OldBookService;
import com.bookhub.book.BookStatus;
import com.bookhub.security.JwtAuthenticationFilter;
import com.bookhub.shared.domain.Isbn;
import com.bookhub.shared.domain.Money;
import com.bookhub.web.dto.BookMapper;
import com.bookhub.web.dto.CreateBookRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.eq;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import com.bookhub.web.dto.BookResponse;
import org.junit.jupiter.api.BeforeEach;

import java.util.Currency;

import static org.mockito.ArgumentMatchers.anyList;

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
@WebMvcTest(BookController.class)
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
    private OldBookService service;


    @MockBean
    private BookMapper mapper;

    @MockBean private JwtAuthenticationFilter jwtAuthenticationFilter;   // ← AJOUTER


    //Avant
    private final BookDto sample = new BookDto(1L, "Effective Java", "Joshua Bloch",
            new Isbn("9780134685991"), Money.of("45.00", "EUR"), BookStatus.AVAILABLE);
    // Après
    CreateBookRequest request = new CreateBookRequest(
            "Effective Java", "Joshua Bloch", "9780134685991",
            new BigDecimal("45.00"), "EUR");

    @BeforeEach
    void setUp() {
        when(mapper.toResponse(any(BookDto.class)))
                .thenAnswer(inv -> {
                    BookDto dto = inv.getArgument(0);
                    return new BookResponse(dto.id(), dto.title(), dto.author(),
                            dto.isbn(), dto.price(), dto.status());
                });
        when(mapper.toResponseList(anyList()))
                .thenAnswer(inv -> {
                    List<BookDto> list = inv.getArgument(0);
                    return list.stream()
                            .map(d -> new BookResponse(d.id(), d.title(), d.author(),
                                    d.isbn(), d.price(), d.status()))
                            .toList();
                });
        when(mapper.toDomain(any(CreateBookRequest.class)))
                .thenAnswer(inv -> {
                    CreateBookRequest r = inv.getArgument(0);
                    return new BookDto(null, r.title(), r.author(),
                            new Isbn(r.isbn()),
                            new Money(r.price(), Currency.getInstance(r.currency())),
                            BookStatus.AVAILABLE);
                });
    }
    @Test
    void should_return_all_books() throws Exception {
        var page = new PageImpl<>(
                List.of(sample),
                PageRequest.of(0, 10),
                1
        );
        when(service.findAll(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title").value("Effective Java"))
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.totalElements").value(1));
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
        when(service.create(any(BookDto.class))).thenReturn(sample);

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Effective Java"));
    }

    /**
     * Ce test doit échouer car @Valid va lever MethodArgumentNotValidException → pas gérée par votre GlobalExceptionHandler actuel.
     *
     * Ajoutez ce handler dans GlobalExceptionHandler :
     * @throws Exception
     */
    @Test
    void should_reject_invalid_create_request() throws Exception {
        String invalid = """
        {
          "title": "",
          "author": "",
          "isbn": "123",
          "price": -5,
          "currency": "EU"
        }
        """;

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalid))
                .andExpect(status().isBadRequest());
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
    @Test
    void should_return_paginated_books() throws Exception {
        var page = new org.springframework.data.domain.PageImpl<>(
                List.of(sample),
                org.springframework.data.domain.PageRequest.of(0, 10),
                1
        );
        when(service.findAll(any(org.springframework.data.domain.Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/api/books?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title").value("Effective Java"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void should_return_404_with_problem_detail() throws Exception {
        when(service.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/books/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Book not found"))
                .andExpect(jsonPath("$.bookId").value(999))
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void should_search_by_title() throws Exception {
        when(service.search(eq("java"), any(), any()))
                .thenReturn(List.of(sample));

        mockMvc.perform(get("/api/books/search?title=java"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Effective Java"));
    }
}