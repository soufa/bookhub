package com.bookhub.book;

import com.bookhub.shared.domain.Isbn;
import com.bookhub.shared.domain.Money;

public record BookDto(
        Long id,
        String title,
        String author,
        Isbn isbn,
        Money price,
        BookStatus status
) {
    public BookDto {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title required");
        }
        if (author == null || author.isBlank()) {
            throw new IllegalArgumentException("Author required");
        }
    }
}