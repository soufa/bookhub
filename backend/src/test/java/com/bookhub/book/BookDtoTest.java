package com.bookhub.book;

import com.bookhub.shared.domain.Isbn;
import com.bookhub.shared.domain.Money;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BookDtoTest {

    @Test
    void should_create_valid_book_dto() {
        BookDto dto = new BookDto(
                1L, "Effective Java", "Joshua Bloch",
                new Isbn("9780134685991"),
                Money.of("45.00", "EUR"),
                BookStatus.AVAILABLE
        );
        assertEquals("Effective Java", dto.title());
    }

    @Test
    void should_reject_blank_title() {
        assertThrows(IllegalArgumentException.class, () -> new BookDto(
                1L, "  ", "Author", new Isbn("9780134685991"),
                Money.of("45.00", "EUR"), BookStatus.AVAILABLE));
    }

    @Test
    void should_reject_null_author() {
        assertThrows(IllegalArgumentException.class, () -> new BookDto(
                1L, "Title", null, new Isbn("9780134685991"),
                Money.of("45.00", "EUR"), BookStatus.AVAILABLE));
    }
}