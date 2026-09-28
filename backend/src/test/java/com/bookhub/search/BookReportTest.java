package com.bookhub.search;

import com.bookhub.book.BookDto;
import com.bookhub.book.BookStatus;
import com.bookhub.shared.domain.Isbn;
import com.bookhub.shared.domain.Money;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BookReportTest {

    private final BookReport report = new BookReport();

    private BookDto book(long id, String title, String author, String price) {
        return new BookDto(id, title, author,
                new Isbn(String.format("%010d", id)), Money.of(price, "EUR"), BookStatus.AVAILABLE);
    }

    private final List<BookDto> books = List.of(
            book(1, "Clean Code", "Robert Martin", "40.00"),
            book(2, "Clean Architecture", "Robert Martin", "35.00"),
            book(3, "Effective Java", "Joshua Bloch", "45.00")
    );

    @Test
    void should_group_titles_by_author() {
        var grouped = report.titlesByAuthor(books);
        assertEquals(2, grouped.get("Robert Martin").size());
        assertEquals(List.of("Effective Java"), grouped.get("Joshua Bloch"));
    }

    @Test
    void should_compute_price_range() {
        var range = report.priceRange(books);
        assertEquals(new BigDecimal("35.00"), range.min());
        assertEquals(new BigDecimal("45.00"), range.max());
    }

    @Test
    void should_return_zero_range_for_empty() {
        var range = report.priceRange(List.of());
        assertEquals(BigDecimal.ZERO, range.min());
    }

    @Test
    void should_join_titles_as_csv() {
        var csv = report.csvTitles(books);
        assertEquals("[Clean Architecture, Clean Code, Effective Java]", csv);
    }
}