package com.bookhub.web.dto;

import com.bookhub.book.BookDto;
import com.bookhub.book.BookStatus;
import com.bookhub.shared.domain.Isbn;
import com.bookhub.shared.domain.Money;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BookMapper {

    public BookDto toDomain(CreateBookRequest request) {
        return new BookDto(
                null,
                request.title().trim(),
                request.author().trim(),
                new Isbn(request.isbn()),
                new Money(request.price(), java.util.Currency.getInstance(request.currency())),
                BookStatus.AVAILABLE
        );
    }

    public BookResponse toResponse(BookDto dto) {
        return new BookResponse(
                dto.id(),
                dto.title(),
                dto.author(),
                dto.isbn(),
                dto.price(),
                dto.status()
        );
    }

    public List<BookResponse> toResponseList(List<BookDto> dtos) {
        return dtos.stream()
                .map(this::toResponse)
                .toList();
    }
}
