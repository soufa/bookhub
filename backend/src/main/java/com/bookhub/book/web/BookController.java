package com.bookhub.book.web;

import com.bookhub.book.BookDto;
import com.bookhub.book.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

/**
 * @RestController = @Controller + @ResponseBody (JSON automatique)
 *
 * @RequestMapping("/api/books") = base URL
 *
 * Injection par constructeur (best practice)
 *
 * ResponseEntity pour contrôler le code HTTP
 */
@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService service;

    public BookController(BookService service) {
        this.service = service;
    }

    @GetMapping
    public List<BookDto> listAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDto> getById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<BookDto> create(@RequestBody BookDto dto) {
        BookDto created = service.create(dto);
        return ResponseEntity
                .created(URI.create("/api/books/" + created.id()))
                .body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookDto> update(@PathVariable Long id, @RequestBody BookDto dto) {
        return service.update(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (service.delete(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}