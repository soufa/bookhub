package com.bookhub.book;

import com.bookhub.author.Author;
import com.bookhub.author.AuthorRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class BookAuthorRelationTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Test
    void should_link_book_to_author() {
        Author author = authorRepository.save(new Author("Joshua Bloch", "US", 1961));

        Book book = new Book("Effective Java", "Joshua Bloch",
                "9780134685991", new BigDecimal("45.00"), "EUR", BookStatus.AVAILABLE);
        book.setAuthorEntity(author);

        Book saved = bookRepository.save(book);
        assertNotNull(saved.getAuthorEntity());
        assertEquals("Joshua Bloch", saved.getAuthorEntity().getName());
    }

    @Test
    void should_load_author_lazily() {
        Author author = authorRepository.save(new Author("Robert Martin", "US", 1952));

        Book book = new Book("Clean Code", "Robert Martin",
                "9780132350884", new BigDecimal("40.00"), "EUR", BookStatus.AVAILABLE);
        book.setAuthorEntity(author);
        bookRepository.save(book);

        Book found = bookRepository.findById(book.getId()).orElseThrow();
        assertEquals("Robert Martin", found.getAuthorEntity().getName());
    }

    @Test
    void should_allow_book_without_author() {
        Book book = new Book("Anonymous", "Unknown",
                "9999999999", new BigDecimal("10.00"), "EUR", BookStatus.AVAILABLE);

        Book saved = bookRepository.save(book);
        assertNull(saved.getAuthorEntity());
    }
}