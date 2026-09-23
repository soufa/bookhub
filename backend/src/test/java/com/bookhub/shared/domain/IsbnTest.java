package com.bookhub.shared.domain;


import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class IsbnTest {
    @Test
    public void should_accept_valid_isbn_10() {
        Isbn isbn= new Isbn("0306406152");
        assertEquals("0306406152", isbn.value());
    }
    @Test
    public void should_accept_valid_isbn_13() {
        Isbn isbn = new Isbn("9780306406157");
        assertEquals("9780306406157", isbn.value());
    }

    @Test
    public void should_clean_dashes_and_spaces() {
        Isbn isbn = new Isbn("978-0-306-40615-7");
        assertEquals("9780306406157", isbn.value());
    }
    @Test
    public void should_reject_invalid_isbn() {
        assertThrows(IllegalArgumentException.class, () -> new Isbn("abc"));
    }

    @Test
    public void should_reject_null() {
        assertThrows(NullPointerException.class, () -> new Isbn(null));
    }
}
