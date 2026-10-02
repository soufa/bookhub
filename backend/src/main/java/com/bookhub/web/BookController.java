package com.bookhub.web;

import com.bookhub.book.BookDto;
import com.bookhub.book.BookNotFoundException;
import com.bookhub.book.OldBookService;
import com.bookhub.book.BookStatus;
import com.bookhub.web.dto.BookMapper;
import com.bookhub.web.dto.BookResponse;
import com.bookhub.web.dto.CreateBookRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final OldBookService service;
    private final BookMapper mapper;

    public BookController(OldBookService service, BookMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    public Page<BookResponse> listAll(
            @PageableDefault(size = 10, sort = "title") Pageable pageable) {
        return service.findAll(pageable)
                .map(mapper::toResponse);
    }

    @GetMapping("/{id}")
    public BookResponse getById(@PathVariable Long id) {
        return service.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new BookNotFoundException(id));
    }

    @GetMapping("/search")
    public List<BookResponse> search(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) BookStatus status) {
        return mapper.toResponseList(service.search(title, author, status));
    }

    @PostMapping
    public ResponseEntity<BookResponse> create(@Valid @RequestBody CreateBookRequest request) {
        BookDto created = service.create(mapper.toDomain(request));
        return ResponseEntity
                .created(URI.create("/api/books/" + created.id()))
                .body(mapper.toResponse(created));
    }

    @PutMapping("/{id}")
    public BookResponse update(@PathVariable Long id, @Valid @RequestBody CreateBookRequest request) {
        BookDto dto = mapper.toDomain(request);
        return service.update(id, dto)
                .map(mapper::toResponse)
                .orElseThrow(() -> new BookNotFoundException(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!service.delete(id)) {
            throw new BookNotFoundException(id);
        }
        return ResponseEntity.noContent().build();
    }
}