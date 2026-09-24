package com.bookhub.book;

import org.junit.jupiter.api.Test;

import static org.junit.Assert.*;

public class BookStatusTest {

        @Test
        void only_available_is_borrowable() {
            assertTrue(BookStatus.AVAILABLE.isBorrowable());
            assertFalse(BookStatus.BORROWED.isBorrowable());
            assertFalse(BookStatus.RESERVED.isBorrowable());
            assertFalse(BookStatus.LOST.isBorrowable());
            assertFalse(BookStatus.MAINTENANCE.isBorrowable());
        }

        @Test
        void description_is_not_blank() {
            for (BookStatus s : BookStatus.values()) {
                assertNotNull(s.description());
                assertFalse(s.description().isBlank());
            }
        }
    }

