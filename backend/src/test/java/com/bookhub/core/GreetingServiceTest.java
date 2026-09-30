package com.bookhub.core;

/*import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;


 * Attention : @SpringBootTest charge TOUT le contexte
 (y compris JPA et la BDD). Comme on n'a pas de BDD de test configurée, ça peut échouer.
 *
 * Solution plus propre : utiliser @ContextConfiguration sans Spring Boot.

@SpringBootTest
@TestPropertySource(properties = {
        "bookhub.greeting.message=Bonjour",
        "bookhub.environment=test"
})
class GreetingServiceTest {

    @Autowired
    private GreetingService greetingService;

    @Test
    void should_inject_custom_properties() {
        assertEquals("Bonjour", greetingService.message());
        assertEquals("test", greetingService.environment());
    }

    @Test
    void should_greet_with_custom_message() {
        String result = greetingService.greet("Alice");
        assertEquals("Bonjour, Alice [test]", result);
    }
}*/

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { GreetingService.class })
class GreetingServiceTest {
    /**
     * Note : avec @ContextConfiguration, @Value ne trouve pas les propriétés par défaut.
     * On peut passer des propriétés via @TestPropertySource ou laisser les valeurs par défaut du @Value.
     */
    @Autowired
    private GreetingService greetingService;

    @Test
    void should_use_default_properties() {
        assertEquals("Hello", greetingService.message());
        assertEquals("dev", greetingService.environment());
    }

    @Test
    void should_greet_with_default_message() {
        String result = greetingService.greet("Alice");
        assertEquals("Hello, Alice [dev]", result);
    }
}