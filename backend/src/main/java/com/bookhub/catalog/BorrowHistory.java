package com.bookhub.catalog;

import com.bookhub.shared.domain.Isbn;

import java.time.Instant;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Objects;

public class BorrowHistory {

    public record BorrowEvent(String user, Isbn isbn, Instant at) {
        public BorrowEvent {
            Objects.requireNonNull(user, "User required");
            Objects.requireNonNull(isbn, "Isbn required");
            Objects.requireNonNull(at, "Date required");
        }
    }

    private final Deque<BorrowEvent> events = new ArrayDeque<>();
    private final int maxSize;


    public BorrowHistory(int maxSize) {
        if (maxSize <= 0) {
            throw new IllegalArgumentException("maxSize must be > 0");
        }
        this.maxSize = maxSize;
    }
    /***
     * Comment BorrowHistory limite-t-elle sa taille en mémoire ?
     * la limite memoire est controlle par maxsize
     */
    public void record(BorrowEvent event) {
        Objects.requireNonNull(event, "Event required");
        events.addLast(event);
        if (events.size() > maxSize) {
            events.removeFirst();
        }
    }

    public List<BorrowEvent> latest(int n) {
        if (n <= 0) return List.of();
        return events.stream()
                .skip(Math.max(0, events.size() - n))
                .toList();
    }

    public int size() {
        return events.size();
    }

    public boolean isEmpty() {
        return events.isEmpty();
    }
}