package com.bookhub.web.dto;

import com.bookhub.book.BookStatus;
import com.bookhub.shared.domain.Isbn;
import com.bookhub.shared.domain.Money;

public record BookWithAuthorDto(
        Long id,
        String title,
        Isbn isbn,
        Money price,
        BookStatus status,
        String authorName,
        String authorNationality
) {}