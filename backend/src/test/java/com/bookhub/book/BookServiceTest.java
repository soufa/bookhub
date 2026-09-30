package com.bookhub.book;

import com.bookhub.shared.domain.Isbn;
import com.bookhub.shared.domain.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BookServiceTest {

    private BookService service;
    private BookDto sample;

    @BeforeEach
    void setUp() {
        service = new BookService();
        sample = new BookDto(null, "Effective Java", "Joshua Bloch",
                new Isbn("9780134685991"), Money.of("45.00", "EUR"), BookStatus.AVAILABLE);
    }

    @Test
    void should_create_and_find_book() {
        BookDto created = service.create(sample);
        assertNotNull(created.id());
        assertEquals(1L, created.id());
        assertTrue(service.findById(1L).isPresent());
    }

    @Test
    void should_return_empty_for_unknown_id() {
        assertTrue(service.findById(999L).isEmpty());
    }

    @Test
    void should_update_book() {
        BookDto created = service.create(sample);
        BookDto update = new BookDto(created.id(), "Effective Java 3rd",
                "Joshua Bloch", new Isbn("9780134685991"),
                Money.of("50.00", "EUR"), BookStatus.BORROWED);

        var result = service.update(created.id(), update);
        assertTrue(result.isPresent());
        assertEquals("Effective Java 3rd", result.get().title());
    }

    @Test
    void should_return_empty_when_updating_unknown() {
        assertTrue(service.update(999L, sample).isEmpty());
    }

    @Test
    void should_delete_book() {
        BookDto created = service.create(sample);
        assertTrue(service.delete(created.id()));
        assertFalse(service.delete(created.id()));
    }

    @Test
    void should_increment_ids() {
        service.create(sample);
        BookDto second = service.create(sample);
        assertEquals(2L, second.id());
    }
}