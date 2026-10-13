package com.bookhub.batch.tasklet;

import com.bookhub.book.Book;
import com.bookhub.book.BookRepository;
import com.bookhub.book.BookStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Tasklet qui calcule les statistiques du catalogue et les persiste
 * dans la table book_statistics.
 */
@Component
public class ComputeStatisticsTasklet implements Tasklet {

    private static final Logger log = LoggerFactory.getLogger(ComputeStatisticsTasklet.class);

    private final BookRepository bookRepository;
    private final JdbcTemplate jdbcTemplate;

    public ComputeStatisticsTasklet(BookRepository bookRepository,
                                    JdbcTemplate jdbcTemplate) {
        this.bookRepository = bookRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution,
                                ChunkContext chunkContext) throws Exception {

        log.info(">>> Computing book statistics...");

        List<Book> books = bookRepository.findAll();
        int total = books.size();

        long available = books.stream()
                .filter(b -> b.getStatus() == BookStatus.AVAILABLE)
                .count();

        long borrowed = books.stream()
                .filter(b -> b.getStatus() == BookStatus.BORROWED)
                .count();

        BigDecimal averagePriceEur = books.stream()
                .filter(b -> "EUR".equals(b.getPriceCurrency()))
                .map(Book::getPriceAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (total > 0) {
            averagePriceEur = averagePriceEur.divide(
                    BigDecimal.valueOf(books.stream()
                            .filter(b -> "EUR".equals(b.getPriceCurrency()))
                            .count()),
                    2, RoundingMode.HALF_UP
            );
        }

        // Top auteur : celui qui a le plus de livres
        Map<String, Long> byAuthor = books.stream()
                .collect(Collectors.groupingBy(Book::getAuthor, Collectors.counting()));

        Map.Entry<String, Long> topAuthor = byAuthor.entrySet().stream()
                .max(Comparator.comparingLong(Map.Entry::getValue))
                .orElse(null);

        String topAuthorName = topAuthor != null ? topAuthor.getKey() : null;
        Integer topAuthorCount = topAuthor != null ? topAuthor.getValue().intValue() : null;

        // Persister le snapshot
        jdbcTemplate.update(
                """
                INSERT INTO book_statistics
                (total_books, available_books, borrowed_books, average_price_eur,
                 top_author, top_author_count)
                VALUES (?, ?, ?, ?, ?, ?)
                """,
                total, available, borrowed, averagePriceEur,
                topAuthorName, topAuthorCount
        );

        log.info(">>> Statistics computed : total={}, available={}, borrowed={}, avgPrice={} EUR, topAuthor={} ({})",
                total, available, borrowed, averagePriceEur, topAuthorName, topAuthorCount);

        return RepeatStatus.FINISHED;
    }
}