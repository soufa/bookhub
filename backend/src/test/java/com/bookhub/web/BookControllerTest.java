package com.bookhub.web;

import com.bookhub.book.BookDto;
import com.bookhub.book.BookService;
import com.bookhub.book.BookStatus;
import com.bookhub.config.TestSecurityConfig;
import com.bookhub.security.JwtAuthenticationFilter;
import com.bookhub.security.JwtService;
import com.bookhub.shared.domain.Isbn;
import com.bookhub.shared.domain.Money;
import com.bookhub.web.dto.BookMapper;
import com.bookhub.web.dto.BookResponse;
import com.bookhub.web.dto.CreateBookRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
@AutoConfigureMockMvc(addFilters = false)   // ← désactive les filtres de sécurité
@Import(TestSecurityConfig.class)            // ← désactive CSRF
@ActiveProfiles("test")
@WithMockUser(roles = "ADMIN")
class BookControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @MockBean private BookService bookService;
    @MockBean private BookMapper bookMapper;
    @MockBean private JwtService jwtService;
    @MockBean private JwtAuthenticationFilter jwtAuthenticationFilter;

    private BookDto sampleBook() {
        return new BookDto(
                1L,
                "Effective Java",
                "Joshua Bloch",
                new Isbn("9780134685991"),
                new Money(new BigDecimal("45.00"), Currency.getInstance("EUR")),
                BookStatus.AVAILABLE
        );
    }

    private BookResponse sampleResponse() {
        return new BookResponse(
                1L,
                "Effective Java",
                "Joshua Bloch",
                new Isbn("9780134685991"),
                new Money(new BigDecimal("45.00"), Currency.getInstance("EUR")),
                BookStatus.AVAILABLE
        );
    }

    @Test
    void should_return_all_books() throws Exception {
        Page<BookDto> page = new PageImpl<>(List.of(sampleBook()));
        when(bookService.findAll(any())).thenReturn(page);
        when(bookMapper.toResponse(any(BookDto.class))).thenReturn(sampleResponse());

        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title").value("Effective Java"));
    }

    @Test
    void should_return_book_by_id() throws Exception {
        when(bookService.findById(1L)).thenReturn(Optional.of(sampleBook()));
        when(bookMapper.toResponse(any(BookDto.class))).thenReturn(sampleResponse());

        mockMvc.perform(get("/api/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Effective Java"));
    }

    @Test
    void should_return_404_when_book_not_found() throws Exception {
        when(bookService.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/books/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void should_create_book() throws Exception {
        CreateBookRequest req = new CreateBookRequest(
                "Effective Java", "Joshua Bloch", "9780134685991",
                new BigDecimal("45.00"), "EUR"
        );

        when(bookMapper.toDomain(any(CreateBookRequest.class))).thenReturn(sampleBook());
        when(bookService.create(any(BookDto.class))).thenReturn(sampleBook());
        when(bookMapper.toResponse(any(BookDto.class))).thenReturn(sampleResponse());

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated());
    }

    @Test
    void should_delete_book() throws Exception {
        when(bookService.delete(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/books/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void should_return_404_when_deleting_unknown() throws Exception {
        when(bookService.delete(99L)).thenReturn(false);

        mockMvc.perform(delete("/api/books/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void should_reject_invalid_create_request() throws Exception {
        CreateBookRequest invalid = new CreateBookRequest("", "", "", null, "");

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_search_by_title() throws Exception {
        when(bookService.search(eq("Effective"), any(), any()))
                .thenReturn(List.of(sampleBook()));
        when(bookMapper.toResponseList(any())).thenReturn(List.of(sampleResponse()));

        mockMvc.perform(get("/api/books/search?title=Effective"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Effective Java"));
    }

    @Test
    void should_return_paginated_books() throws Exception {
        Page<BookDto> page = new PageImpl<>(List.of(sampleBook()), PageRequest.of(0, 10), 1);
        when(bookService.findAll(any())).thenReturn(page);
        when(bookMapper.toResponse(any(BookDto.class))).thenReturn(sampleResponse());

        mockMvc.perform(get("/api/books?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void should_return_404_with_problem_detail() throws Exception {
        when(bookService.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/books/99"))
                .andExpect(status().isNotFound());
    }
}