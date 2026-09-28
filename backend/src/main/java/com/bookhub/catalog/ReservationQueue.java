package com.bookhub.catalog;

import com.bookhub.shared.domain.Isbn;

import java.time.Instant;
import java.util.Comparator;
import java.util.Objects;
import java.util.PriorityQueue;

public class ReservationQueue {

    public record Reservation(String user, Isbn isbn, Instant requestedAt) {
        public Reservation {
            Objects.requireNonNull(user, "User required");
            Objects.requireNonNull(isbn, "Isbn required");
            Objects.requireNonNull(requestedAt, "Date required");
        }
    }

    private final PriorityQueue<Reservation> queue =
            new PriorityQueue<>(Comparator.comparing(Reservation::requestedAt));

    public void enqueue(Reservation reservation) {
        Objects.requireNonNull(reservation, "Reservation required");
        queue.offer(reservation);
    }

    public Reservation next() {
        return queue.poll();
    }

    public Reservation peek() {
        return queue.peek();
    }

    public int size() {
        return queue.size();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}