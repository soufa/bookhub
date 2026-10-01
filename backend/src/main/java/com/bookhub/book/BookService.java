package com.bookhub.book;

import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import java.util.Comparator;
import java.util.*;
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
    public Page<BookDto> findAll(Pageable pageable) {
        List<BookDto> all = new ArrayList<>(books.values());
        all.sort(Comparator.comparing(BookDto::title));

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), all.size());

        if (start > all.size()) {
            return new PageImpl<>(List.of(), pageable, all.size());
        }

        return new PageImpl<>(all.subList(start, end), pageable, all.size());
    }

    public List<BookDto> search(String title, String author, BookStatus status) {
        return books.values().stream()
                .filter(b -> title == null || b.title().toLowerCase().contains(title.toLowerCase()))
                .filter(b -> author == null || b.author().toLowerCase().contains(author.toLowerCase()))
                .filter(b -> status == null || b.status() == status)
                .sorted(Comparator.comparing(BookDto::title))
                .toList();
    }
}