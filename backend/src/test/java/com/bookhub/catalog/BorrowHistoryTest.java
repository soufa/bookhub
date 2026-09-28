package com.bookhub.catalog;

import com.bookhub.shared.domain.Isbn;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BorrowHistoryTest {

    private BorrowHistory.BorrowEvent ev(String user, long seconds) {
        return new BorrowHistory.BorrowEvent(user, new Isbn("9780134685991"),
                Instant.ofEpochSecond(seconds));
    }

    @Test
    void should_keep_last_events_only() {
        var history = new BorrowHistory(3);
        history.record(ev("A", 1));
        history.record(ev("B", 2));
        history.record(ev("C", 3));
        history.record(ev("D", 4));

        assertEquals(3, history.size());
        var latest = history.latest(3);
        assertEquals("B", latest.get(0).user());
        assertEquals("D", latest.get(2).user());
    }

    @Test
    void should_return_empty_when_no_events() {
        var history = new BorrowHistory(5);
        assertTrue(history.isEmpty());
        assertEquals(0, history.latest(2).size());
    }

    @Test
    void should_reject_invalid_max_size() {
        assertThrows(IllegalArgumentException.class, () -> new BorrowHistory(0));
    }
}