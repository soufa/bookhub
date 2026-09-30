package com.bookhub.book;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @Service : Spring le détecte au scan
 *
 * ConcurrentHashMap : thread-safe (plusieurs requêtes HTTP simultanées)
 *
 * AtomicLong : générateur d'ID sans race condition
 */
@Service
public class BookService {

    private final Map<Long, BookDto> books = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public List<BookDto> findAll() {
        return new ArrayList<>(books.values());
    }

    public Optional<BookDto> findById(Long id) {
        return Optional.ofNullable(books.get(id));
    }

    public BookDto create(BookDto dto) {
        Long id = idGenerator.getAndIncrement();
        BookDto persisted = new BookDto(
                id, dto.title(), dto.author(), dto.isbn(), dto.price(), dto.status());
        books.put(id, persisted);
        return persisted;
    }

    public Optional<BookDto> update(Long id, BookDto dto) {
        if (!books.containsKey(id)) {
            return Optional.empty();
        }
        BookDto updated = new BookDto(
                id, dto.title(), dto.author(), dto.isbn(), dto.price(), dto.status());
        books.put(id, updated);
        return Optional.of(updated);
    }

    public boolean delete(Long id) {
        return books.remove(id) != null;
    }

    public int size() {
        return books.size();
    }
}