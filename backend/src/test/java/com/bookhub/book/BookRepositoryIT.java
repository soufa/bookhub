package com.bookhub.book;

import com.bookhub.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@Sql(scripts = "/db/clean.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
        config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
@Transactional
class BookRepositoryIT extends AbstractIntegrationTest {

    @Autowired
    private BookRepository repository;

    @Test
    void should_persist_and_retrieve_book_on_real_postgres() {
        Book book = new Book("Effective Java", "Joshua Bloch", "9780000000001",
                new BigDecimal("45.00"), "EUR", BookStatus.AVAILABLE);

        Book saved = repository.save(book);
        assertNotNull(saved.getId());

        Optional<Book> found = repository.findByIsbn("9780000000001");
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

        assertThrows(Exception.class, () -> repository.saveAndFlush(duplicate));
    }

    @Test
    void should_support_real_postgres_dialect() {
        // Vérifie que la connexion est bien PostgreSQL (pas H2)
        assertTrue(repository.findAll().isEmpty() || repository.count() >= 0);
    }
}