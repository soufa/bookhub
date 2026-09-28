package com.bookhub.catalog;

import com.bookhub.shared.domain.Isbn;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class ReservationQueueTest {

    private ReservationQueue.Reservation res(String user, long seconds) {
        return new ReservationQueue.Reservation(user,
                new Isbn("9780134685991"), Instant.ofEpochSecond(seconds));
    }

    @Test
    void should_return_oldest_reservation_first() {
        var q = new ReservationQueue();
        q.enqueue(res("Charlie", 300));
        q.enqueue(res("Alice", 100));
        q.enqueue(res("Bob", 200));

        assertEquals("Alice", q.next().user());
        assertEquals("Bob", q.next().user());
        assertEquals("Charlie", q.next().user());
    }

    @Test
    void should_return_null_when_empty() {
        var q = new ReservationQueue();
        assertTrue(q.isEmpty());
        assertNull(q.next());
    }

    @Test
    void should_track_size() {
        var q = new ReservationQueue();
        q.enqueue(res("A", 1));
        q.enqueue(res("B", 2));
        assertEquals(2, q.size());
        assertEquals(2, q.size());
    }
}