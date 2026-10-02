package com.bookhub.book;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class BookRepositoryTest {

    @Autowired
    private BookRepository repository;

    private Book book(String title, String author, String isbn, BookStatus status) {
        return new Book(title, author, isbn, new BigDecimal("45.00"), "EUR", status);
    }

    @Test
    void should_save_and_find_book_by_id() {
        Book saved = repository.save(
                book("Effective Java", "Joshua Bloch", "9780134685991", BookStatus.AVAILABLE));

        assertNotNull(saved.getId());

        Optional<Book> found = repository.findById(saved.getId());
        assertTrue(found.isPresent());
        assertEquals("Effective Java", found.get().getTitle());
    }

    @Test
    void should_find_by_isbn() {
        repository.save(
                book("Effective Java", "Joshua Bloch", "9780134685991", BookStatus.AVAILABLE));

        Optional<Book> found = repository.findByIsbn("9780134685991");
        assertTrue(found.isPresent());
        assertEquals("Effective Java", found.get().getTitle());
    }

    @Test
    void should_check_isbn_uniqueness() {
        repository.save(
                book("Book 1", "Author 1", "9780134685991", BookStatus.AVAILABLE));

        assertTrue(repository.existsByIsbn("9780134685991"));
        assertFalse(repository.existsByIsbn("0000000000"));
    }

    @Test
    void should_find_by_status() {
        repository.save(book("A", "X", "9780134685991", BookStatus.AVAILABLE));
        repository.save(book("B", "Y", "9780132350884", BookStatus.BORROWED));
        repository.save(book("C", "Z", "9780321125217", BookStatus.AVAILABLE));

        List<Book> available = repository.findByStatus(BookStatus.AVAILABLE);
        assertEquals(2, available.size());
    }

    @Test
    void should_find_by_author_case_insensitive() {
        repository.save(
                book("Effective Java", "Joshua Bloch", "9780134685991", BookStatus.AVAILABLE));

        List<Book> found = repository.findByAuthorContainingIgnoreCase("joshua");
        assertEquals(1, found.size());
    }

    @Test
    void should_set_timestamps_on_persist() {
        Book saved = repository.save(
                book("Effective Java", "Joshua Bloch", "9780134685991", BookStatus.AVAILABLE));

        assertNotNull(saved.getCreatedAt());
        assertNotNull(saved.getUpdatedAt());
        assertEquals(saved.getCreatedAt(), saved.getUpdatedAt());
    }
}