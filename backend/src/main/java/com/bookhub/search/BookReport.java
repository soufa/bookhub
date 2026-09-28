package com.bookhub.search;

import com.bookhub.book.BookDto;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BookReport {

    public record PriceRange(BigDecimal min, BigDecimal max) {}

    public Map<String, List<String>> titlesByAuthor(List<BookDto> books) {
        return books.stream()
                .collect(Collectors.groupingBy(
                        BookDto::author,
                        Collectors.mapping(BookDto::title, Collectors.toList())
                ));
    }

    public PriceRange priceRange(List<BookDto> books) {
        if (books.isEmpty()) {
            return new PriceRange(BigDecimal.ZERO, BigDecimal.ZERO);
        }

        return books.stream()
                .collect(Collectors.teeing(
                        Collectors.minBy(Comparator.comparing(b -> b.price().amount())),
                        Collectors.maxBy(Comparator.comparing(b -> b.price().amount())),
                        (minOpt, maxOpt) -> new PriceRange(
                                minOpt.map(b -> b.price().amount()).orElse(BigDecimal.ZERO),
                                maxOpt.map(b -> b.price().amount()).orElse(BigDecimal.ZERO)
                        )
                ));
    }

    public String csvTitles(List<BookDto> books) {
        return books.stream()
                .map(BookDto::title)
                .sorted()
                .collect(Collectors.joining(", ", "[", "]"));
    }
}