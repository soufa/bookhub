package com.bookhub.catalog;

import com.bookhub.book.BookDto;
import com.bookhub.book.BookStatus;

import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;

public class CatalogStatistics {

    private final EnumMap<BookStatus, Long> countsByStatus = new EnumMap<>(BookStatus.class);

    public CatalogStatistics(Collection<BookDto> books) {
        for (BookStatus status : BookStatus.values()) {
            countsByStatus.put(status, 0L);
        }
        for (BookDto book : books) {
            countsByStatus.merge(book.status(), 1L, Long::sum);
        }
    }

    public long countOf(BookStatus status) {
        return countsByStatus.getOrDefault(status, 0L);
    }

    public Map<BookStatus, Long> asMap() {
        return Map.copyOf(countsByStatus);
    }

    public long total() {
        return countsByStatus.values().stream().mapToLong(Long::longValue).sum();
    }
}