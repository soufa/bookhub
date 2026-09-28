package com.bookhub.search;

import com.bookhub.book.BookDto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BookStatistics {

    public BigDecimal averagePrice(List<BookDto> books) {
        if (books.isEmpty()) return BigDecimal.ZERO;

        BigDecimal total = books.stream()
                .map(b -> b.price().amount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return total.divide(BigDecimal.valueOf(books.size()), 2, RoundingMode.HALF_UP);
    }

    public Map<String, Long> countByAuthor(List<BookDto> books) {
        return books.stream()
                .collect(Collectors.groupingBy(
                        BookDto::author,
                        Collectors.counting()
                ));
    }

    public List<String> topAuthors(List<BookDto> books, int n) {
        return countByAuthor(books).entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed()
                        .thenComparing(Map.Entry.comparingByKey()))
                .limit(n)
                .map(Map.Entry::getKey)
                .toList();
    }

    public long countAvailable(List<BookDto> books) {
        return books.stream()
                .filter(b -> b.status().isBorrowable())
                .count();
    }
}