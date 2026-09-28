package com.bookhub.search;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class PriceCategoryCollector {

    private PriceCategoryCollector() {}

    public static Collector<BigDecimal, ?, Map<String, Long>> byCategory() {
        return Collector.of(
                HashMap::new,
                (map, price) -> {
                    String category = categorize(price);
                    map.merge(category, 1L, Long::sum);
                },
                (left, right) -> {
                    right.forEach((k, v) -> left.merge(k, v, Long::sum));
                    return left;
                },
                Collector.Characteristics.UNORDERED
        );
    }

    private static String categorize(BigDecimal price) {
        if (price.compareTo(new BigDecimal("30")) < 0) return "CHEAP";
        if (price.compareTo(new BigDecimal("50")) < 0) return "MEDIUM";
        return "EXPENSIVE";
    }
}