package com.bookhub.book;

import com.bookhub.shared.domain.Isbn;
import com.bookhub.shared.domain.Money;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Currency;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private final BookRepository repository;

    public BookService(BookRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public Page<BookDto> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(this::toDto);
    }

    @Transactional(readOnly = true)
    public Optional<BookDto> findById(Long id) {
        return repository.findById(id).map(this::toDto);
    }

    @Transactional(readOnly = true)
    public List<BookDto> search(String title, String author, BookStatus status) {
        if (title != null && !title.isBlank()) {
            return repository.findByTitleContainingIgnoreCase(title)
                    .stream().map(this::toDto).toList();
        }
        if (author != null && !author.isBlank()) {
            return repository.findByAuthorContainingIgnoreCase(author)
                    .stream().map(this::toDto).toList();
        }
        if (status != null) {
            return repository.findByStatus(status)
                    .stream().map(this::toDto).toList();
        }
        return repository.findAll().stream().map(this::toDto).toList();
    }

    @Transactional
    public BookDto create(BookDto dto) {
        if (repository.existsByIsbn(dto.isbn().value())) {
            throw new IllegalArgumentException(
                    "Book with ISBN " + dto.isbn().value() + " already exists");
        }

        Book entity = new Book(
                dto.title(),
                dto.author(),
                dto.isbn().value(),
                dto.price().amount(),
                dto.price().currency().getCurrencyCode(),
                dto.status()
        );

        Book saved = repository.save(entity);
        return toDto(saved);
    }

    @Transactional
    public Optional<BookDto> update(Long id, BookDto dto) {
        return repository.findById(id).map(entity -> {
            entity.setTitle(dto.title());
            entity.setAuthor(dto.author());
            entity.setStatus(dto.status());
            entity.setPrice(
                    dto.price().amount(),
                    dto.price().currency().getCurrencyCode()
            );
            return toDto(entity);
        });
    }

    @Transactional
    public boolean delete(Long id) {
        if (!repository.existsById(id)) {
            return false;
        }
        repository.deleteById(id);
        return true;
    }

    @Transactional(readOnly = true)
    public int size() {
        return (int) repository.count();
    }

    private BookDto toDto(Book entity) {
        return new BookDto(
                entity.getId(),
                entity.getTitle(),
                entity.getAuthor(),
                new Isbn(entity.getIsbn()),
                new Money(
                        entity.getPriceAmount(),
                        Currency.getInstance(entity.getPriceCurrency())
                ),
                entity.getStatus()
        );
    }
}