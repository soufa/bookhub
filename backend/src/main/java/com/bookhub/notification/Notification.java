package com.bookhub.notification;

public sealed interface Notification permits EmailNotification, SmsNotification {
    String recipient();
    String content();
}