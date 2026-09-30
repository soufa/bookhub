package com.bookhub.core;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.time.ZoneId;

/**
 * @Configuration : classe de configuration Spring
 *
 * @Bean : déclare un bean produit par une méthode
 *
 * Utile pour les types que vous ne pouvez pas annoter (Clock, String, etc.)
 */
@Configuration
public class AppConfig {

    @Bean
    public Clock systemClock() {
        return Clock.system(ZoneId.of("Europe/Paris"));
    }

    @Bean
    public String applicationName() {
        return "BookHub";
    }
}