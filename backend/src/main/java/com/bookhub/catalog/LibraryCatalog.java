package com.bookhub.catalog;

import com.bookhub.book.BookDto;
import com.bookhub.book.BookStatus;
import com.bookhub.shared.domain.Isbn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class LibraryCatalog {

    private final Map<Isbn, BookDto> books = new HashMap<>();

    public void add(BookDto book) {
        Objects.requireNonNull(book, "Book required");
        books.put(book.isbn(), book);
    }

    public Optional<BookDto> findByIsbn(Isbn isbn) {
        return Optional.ofNullable(books.get(isbn));
    }

    public List<BookDto> findByStatus(BookStatus status) {
        return books.values().stream()
                .filter(b -> b.status() == status)
                .toList();
    }

    public List<BookDto> findAll() {
        return new ArrayList<>(books.values());
    }

    public int size() {
        return books.size();
    }

    public boolean remove(Isbn isbn) {
        return books.remove(isbn) != null;
    }
}