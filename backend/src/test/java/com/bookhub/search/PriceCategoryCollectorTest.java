package com.bookhub.search;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PriceCategoryCollectorTest {

    @Test
    void should_categorize_prices() {
        var prices = List.of(
                new BigDecimal("10.00"),
                new BigDecimal("25.00"),
                new BigDecimal("35.00"),
                new BigDecimal("45.00"),
                new BigDecimal("60.00")
        );

        Map<String, Long> result = prices.stream()
                .collect(PriceCategoryCollector.byCategory());

        assertEquals(2L, result.get("CHEAP"));
        assertEquals(2L, result.get("MEDIUM"));
        assertEquals(1L, result.get("EXPENSIVE"));
    }

    @Test
    void should_handle_empty_stream() {
        var result = List.<BigDecimal>of().stream()
                .collect(PriceCategoryCollector.byCategory());
        assertEquals(0, result.size());
    }
}