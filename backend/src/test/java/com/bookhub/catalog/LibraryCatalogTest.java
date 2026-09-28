package com.bookhub.catalog;

import com.bookhub.book.BookDto;
import com.bookhub.book.BookStatus;
import com.bookhub.shared.domain.Isbn;
import com.bookhub.shared.domain.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LibraryCatalogTest {

    private LibraryCatalog catalog;
    private BookDto effectiveJava;
    private BookDto cleanCode;

    @BeforeEach
    void setUp() {
        catalog = new LibraryCatalog();
        effectiveJava = new BookDto(1L, "Effective Java", "Joshua Bloch",
                new Isbn("9780134685991"), Money.of("45.00", "EUR"), BookStatus.AVAILABLE);
        cleanCode = new BookDto(2L, "Clean Code", "Robert Martin",
                new Isbn("9780132350884"), Money.of("40.00", "EUR"), BookStatus.BORROWED);
    }

    @Test
    void should_add_and_find_book() {
        catalog.add(effectiveJava);
        var found = catalog.findByIsbn(new Isbn("9780134685991"));
        assertTrue(found.isPresent());
        assertEquals("Effective Java", found.get().title());
    }

    @Test
    void should_return_empty_for_unknown_isbn() {
        var found = catalog.findByIsbn(new Isbn("0000000000"));
        assertTrue(found.isEmpty());
    }

    @Test
    void should_find_by_status() {
        catalog.add(effectiveJava);
        catalog.add(cleanCode);
        var available = catalog.findByStatus(BookStatus.AVAILABLE);
        assertEquals(1, available.size());
        assertEquals("Effective Java", available.get(0).title());
    }

    @Test
    void should_remove_book() {
        catalog.add(effectiveJava);
        assertTrue(catalog.remove(new Isbn("9780134685991")));
        assertFalse(catalog.remove(new Isbn("9780134685991")));
        assertEquals(0, catalog.size());
    }

    @Test
    void should_reject_null() {
        assertThrows(NullPointerException.class, () -> catalog.add(null));
    }
}