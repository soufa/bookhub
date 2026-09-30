package com.bookhub.core;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GreetingService {

    private final String message;
    private final String environment;

    public GreetingService(
            @Value("${bookhub.greeting.message:Hello}") String message,
            @Value("${bookhub.environment:dev}") String environment) {
        this.message = message;
        this.environment = environment;
    }

    public String greet(String name) {
        return message + ", " + name + " [" + environment + "]";
    }

    public String message() { return message; }
    public String environment() { return environment; }
}