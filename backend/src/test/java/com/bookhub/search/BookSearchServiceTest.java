package com.bookhub.search;

import com.bookhub.book.BookDto;
import com.bookhub.book.BookStatus;
import com.bookhub.shared.domain.Isbn;
import com.bookhub.shared.domain.Money;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BookSearchServiceTest {

    private final BookSearchService service = new BookSearchService();

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
    void should_return_all_with_empty_criteria() {
        var result = service.search(books, BookSearchService.SearchCriteria.empty());
        assertEquals(4, result.size());
    }

    @Test
    void should_filter_by_title() {
        var criteria = new BookSearchService.SearchCriteria("clean", null, null, null);
        var result = service.search(books, criteria);
        assertEquals(2, result.size());
    }

    @Test
    void should_filter_by_author() {
        var criteria = new BookSearchService.SearchCriteria(null, "martin", null, null);
        var result = service.search(books, criteria);
        assertEquals(2, result.size());
    }

    @Test
    void should_filter_by_status() {
        var criteria = new BookSearchService.SearchCriteria(null, null, BookStatus.AVAILABLE, null);
        var result = service.search(books, criteria);
        assertEquals(3, result.size());
    }

    @Test
    void should_filter_by_max_price() {
        var criteria = new BookSearchService.SearchCriteria(null, null, null, new BigDecimal("42.00"));
        var result = service.search(books, criteria);
        assertEquals(2, result.size());
    }

    @Test
    void should_combine_criteria() {
        var criteria = new BookSearchService.SearchCriteria(
                "clean", "martin", BookStatus.AVAILABLE, new BigDecimal("40.00"));
        var result = service.search(books, criteria);
        assertEquals(1, result.size());
        assertEquals("Clean Architecture", result.get(0).title());
    }

    @Test
    void should_sort_by_title() {
        var result = service.search(books, BookSearchService.SearchCriteria.empty());
        assertEquals("Clean Architecture", result.get(0).title());
        assertEquals("Java Concurrency", result.get(3).title());
    }
}