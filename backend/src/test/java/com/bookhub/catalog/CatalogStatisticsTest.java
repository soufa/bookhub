package com.bookhub.catalog;

import com.bookhub.book.BookDto;
import com.bookhub.book.BookStatus;
import com.bookhub.shared.domain.Isbn;
import com.bookhub.shared.domain.Money;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CatalogStatisticsTest {

    private BookDto book(long id, BookStatus status) {
        return new BookDto(id, "Title" + id, "Author",
                new Isbn(String.format("%010d", id)), Money.of("10.00", "EUR"), status);
    }

    @Test
    void should_count_by_status() {
        var books = List.of(
                book(1, BookStatus.AVAILABLE),
                book(2, BookStatus.AVAILABLE),
                book(3, BookStatus.BORROWED),
                book(4, BookStatus.LOST)
        );
        var stats = new CatalogStatistics(books);
        assertEquals(2, stats.countOf(BookStatus.AVAILABLE));
        assertEquals(1, stats.countOf(BookStatus.BORROWED));
        assertEquals(0, stats.countOf(BookStatus.RESERVED));
        assertEquals(1, stats.countOf(BookStatus.LOST));
        assertEquals(4, stats.total());
    }

    @Test
    void should_handle_empty_catalog() {
        var stats = new CatalogStatistics(List.of());
        assertEquals(0, stats.total());
        assertEquals(0, stats.countOf(BookStatus.AVAILABLE));
    }
}