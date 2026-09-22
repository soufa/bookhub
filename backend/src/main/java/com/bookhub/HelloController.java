package com.bookhub;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;

@RestController
public class HelloController {

    @GetMapping("/api/hello")
    public Map<String, Object> hello() {
        return Map.of(
                "message", "BookHub is alive",
                "timestamp", Instant.now().toString(),
                "java", System.getProperty("java.version")
        );
    }
}