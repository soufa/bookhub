package com.bookhub.notification;

import org.springframework.stereotype.Service;

@Service
public class NotificationDispatcher {

    public String format(Notification notification) {
        if (notification instanceof EmailNotification email) {
            return "[EMAIL → " + email.email() + "] " + email.subject();
        }
        if (notification instanceof SmsNotification sms) {
            return "[SMS → " + sms.phoneNumber() + "]";
        }
        throw new IllegalStateException("Unknown notification: " + notification.getClass());
    }
}
