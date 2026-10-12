package com.bookhub.batch.processor;

import com.bookhub.batch.dto.BookCsvRecord;
import com.bookhub.book.Book;
import com.bookhub.book.BookRepository;
import com.bookhub.book.BookStatus;
import com.bookhub.shared.domain.Isbn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Currency;

@Component
public class BookItemProcessor implements ItemProcessor<BookCsvRecord, Book> {

    private static final Logger log = LoggerFactory.getLogger(BookItemProcessor.class);

    private final BookRepository bookRepository;

    public BookItemProcessor(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Book process(BookCsvRecord record) throws Exception {
        // Validations basiques
        if (record.getTitle() == null || record.getTitle().isBlank()) {
            log.warn("Titre vide, item ignoré : {}", record);
            return null;
        }
        if (record.getIsbn() == null || record.getIsbn().isBlank()) {
            log.warn("ISBN vide, item ignoré : {}", record);
            return null;
        }

        // Validation ISBN
        Isbn isbn;
        try {
            isbn = new Isbn(record.getIsbn());
        } catch (IllegalArgumentException e) {
            log.warn("ISBN invalide '{}', item ignoré", record.getIsbn());
            return null;
        }

        // Validation devise
        Currency currency;
        try {
            currency = Currency.getInstance(record.getCurrency());
        } catch (IllegalArgumentException e) {
            log.warn("Devise invalide '{}', item ignoré", record.getCurrency());
            return null;
        }

        BigDecimal price = record.getPrice();
        if (price == null) {
            log.warn("Prix null, item ignoré : {}", record);
            return null;
        }

        // Vérification doublon (le fix)
        if (bookRepository.existsByIsbn(isbn.value())) {
            log.info("ISBN {} déjà présent, skip", isbn.value());
            return null;
        }

        // Créer l'entité Book
        Book book = new Book(
                record.getTitle().trim(),
                record.getAuthor() != null ? record.getAuthor().trim() : "Inconnu",
                isbn.value(),
                price,
                currency.getCurrencyCode(),
                BookStatus.AVAILABLE
        );

        log.debug("Processed book : '{}'", book.getTitle());
        return book;
    }
}