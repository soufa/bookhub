package com.bookhub.batch.dto;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Résultat du job computeStatisticsJob.
 * Représente les statistiques du catalogue à un instant donné.
 */
public record StatisticsDto(
        Instant computedAt,
        int totalBooks,
        int availableBooks,
        int borrowedBooks,
        BigDecimal averagePriceEur,
        String topAuthor,
        Integer topAuthorCount
) {}