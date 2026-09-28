package com.bookhub.search;

import com.bookhub.book.BookDto;
import com.bookhub.book.BookStatus;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public class BookSearchService {

    public record SearchCriteria(
            String titleContains,
            String authorContains,
            BookStatus status,
            BigDecimal maxPrice
    ) {
        public static SearchCriteria empty() {
            return new SearchCriteria(null, null, null, null);
        }
    }

    public List<BookDto> search(List<BookDto> books, SearchCriteria criteria) {
        return books.stream()
                .filter(buildPredicate(criteria))
                .sorted(Comparator.comparing(BookDto::title))
                .toList();
    }

    private Predicate<BookDto> buildPredicate(SearchCriteria criteria) {
        Predicate<BookDto> predicate = book -> true;

        if (criteria.titleContains() != null && !criteria.titleContains().isBlank()) {
            String needle = criteria.titleContains().toLowerCase();
            predicate = predicate.and(b ->
                    b.title().toLowerCase().contains(needle));
        }

        if (criteria.authorContains() != null && !criteria.authorContains().isBlank()) {
            String needle = criteria.authorContains().toLowerCase();
            predicate = predicate.and(b ->
                    b.author().toLowerCase().contains(needle));
        }

        if (criteria.status() != null) {
            predicate = predicate.and(b -> b.status() == criteria.status());
        }

        if (criteria.maxPrice() != null) {
            predicate = predicate.and(b ->
                    b.price().amount().compareTo(criteria.maxPrice()) <= 0);
        }

        return predicate;
    }
}