package com.bookhub.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record CreateBookRequest(

        @NotBlank(message = "Title is required")
        @Size(max = 200, message = "Title must be <= 200 chars")
        String title,

        @NotBlank(message = "Author is required")
        @Size(max = 100, message = "Author must be <= 100 chars")
        String author,

        @NotBlank(message = "ISBN is required")
        String isbn,

        @NotNull(message = "Price is required")
        @Positive(message = "Price must be positive")
        BigDecimal price,

        @NotBlank(message = "Currency is required")
        @Size(min = 3, max = 3, message = "Currency must be ISO 4217 (3 letters)")
        String currency
) {}