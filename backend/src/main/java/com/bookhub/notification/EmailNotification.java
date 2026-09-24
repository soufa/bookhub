package com.bookhub.notification;

public record EmailNotification(String email, String subject, String body)
        implements Notification {

    public EmailNotification {
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Invalid email");
        }
    }

    @Override public String recipient() { return email; }
    @Override public String content() { return subject + ": " + body; }
}
