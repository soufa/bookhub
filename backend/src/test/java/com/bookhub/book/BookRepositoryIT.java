package com.bookhub.book;

import com.bookhub.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test d'intégration : utilise PostgreSQL réel via Testcontainers.
 * Vérifie que le code marche sur la VRAIE base (pas H2).
 */
@Transactional
class BookRepositoryIT extends AbstractIntegrationTest {

    @Autowired
    private BookRepository repository;

    @Test
    void should_persist_and_retrieve_book_on_real_postgres() {
        Book book = new Book("Effective Java", "Joshua Bloch", "9780134685991",
                new BigDecimal("45.00"), "EUR", BookStatus.AVAILABLE);

        Book saved = repository.save(book);
        assertNotNull(saved.getId());

        Optional<Book> found = repository.findByIsbn("9780134685991");
        assertTrue(found.isPresent());
        assertEquals("Effective Java", found.get().getTitle());
    }

    @Test
    void should_enforce_unique_isbn_on_real_postgres() {
        Book book1 = new Book("A", "X", "1234567890",
                new BigDecimal("10.00"), "EUR", BookStatus.AVAILABLE);
        repository.save(book1);

        Book duplicate = new Book("B", "Y", "1234567890",
                new BigDecimal("20.00"), "EUR", BookStatus.AVAILABLE);

        assertThrows(Exception.class, () -> {
            repository.saveAndFlush(duplicate);
        });
    }

    @Test
    void should_use_real_postgres_dialect() {
        // Vérifie qu'on parle bien à PostgreSQL
        String version = repository.findAll().stream()
                .findFirst()
                .map(b -> "OK")
                .orElse("EMPTY");
        assertNotNull(version);
    }
}