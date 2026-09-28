package com.bookhub.search;

import com.bookhub.book.BookDto;
import com.bookhub.book.BookStatus;
import com.bookhub.shared.domain.Isbn;
import com.bookhub.shared.domain.Money;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BookStatisticsTest {

    private final BookStatistics stats = new BookStatistics();

    private BookDto book(long id, String title, String author, String price, BookStatus status) {
        return new BookDto(id, title, author,
                new Isbn(String.format("%010d", id)), Money.of(price, "EUR"), status);
    }

    private final List<BookDto> books = List.of(
            book(1, "Effective Java", "Joshua Bloch", "45.00", BookStatus.AVAILABLE),
            book(2, "Clean Code", "Robert Martin", "40.00", BookStatus.BORROWED),
            book(3, "Java Concurrency", "Brian Goetz", "55.00", BookStatus.AVAILABLE),
            book(4, "Clean Architecture", "Robert Martin", "35.00", BookStatus.AVAILABLE)
    );

    @Test
    void should_compute_average_price() {
        var avg = stats.averagePrice(books);
        assertEquals(new BigDecimal("43.75"), avg);
    }

    @Test
    void should_return_zero_average_for_empty() {
        assertEquals(BigDecimal.ZERO, stats.averagePrice(List.of()));
    }

    @Test
    void should_count_by_author() {
        var counts = stats.countByAuthor(books);
        assertEquals(2L, counts.get("Robert Martin"));
        assertEquals(1L, counts.get("Joshua Bloch"));
    }

    @Test
    void should_return_top_authors() {
        var top = stats.topAuthors(books, 2);
        assertEquals("Robert Martin", top.get(0));
        assertEquals(2, top.size());
    }

    @Test
    void should_count_available_books() {
        assertEquals(3L, stats.countAvailable(books));
    }
}