package com.bookhub.web.dto;

import com.bookhub.book.BookStatus;
import com.bookhub.shared.domain.Isbn;
import com.bookhub.shared.domain.Money;

public record BookResponse(
        Long id,
        String title,
        String author,
        Isbn isbn,
        Money price,
        BookStatus status
) {}
