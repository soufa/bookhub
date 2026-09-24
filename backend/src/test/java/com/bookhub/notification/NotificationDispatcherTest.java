package com.bookhub.notification;

import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

class NotificationDispatcherTest {

    private final NotificationDispatcher dispatcher = new NotificationDispatcher();

    @Test
    void should_format_email() {
        var email = new EmailNotification("user@x.io", "Welcome", "Hi");
        assertTrue(dispatcher.format(email).startsWith("[EMAIL"));
    }

    @Test
    void should_format_sms() {
        var sms = new SmsNotification("+33612345678", "Hello");
        assertTrue(dispatcher.format(sms).startsWith("[SMS"));
    }

    @Test
    void should_reject_invalid_email() {
        assertThrows(IllegalArgumentException.class,
                () -> new EmailNotification("not-an-email", "s", "b"));
    }

    @Test
    void should_reject_invalid_phone() {
        assertThrows(IllegalArgumentException.class,
                () -> new SmsNotification("abc", "msg"));
    }
}
