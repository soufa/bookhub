package com.bookhub.notification;

public record SmsNotification(String phoneNumber, String message)
        implements Notification {

    public SmsNotification {
        if (phoneNumber == null || !phoneNumber.matches("\\+?\\d{6,15}")) {
            throw new IllegalArgumentException("Invalid phone number");
        }
    }

    @Override public String recipient() { return phoneNumber; }
    @Override public String content() { return message; }
}
