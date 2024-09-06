package ru.otus.hw.controllers;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookRequestDto;
import ru.otus.hw.services.BookService;

import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Log4j2
public class BookController {

    private final BookService bookService;

    @GetMapping("/api/books")
    @CircuitBreaker(name = "bookServiceCircuitBreaker", fallbackMethod = "fallbackGetBooks")
    public ResponseEntity<List<BookDto>> getBooks() {
        List<BookDto> books = bookService.findAll();
        return ResponseEntity.ok(books);
    }

    @GetMapping("/api/books/{id}")
    @CircuitBreaker(name = "bookServiceCircuitBreaker", fallbackMethod = "fallbackGetBook")
    public ResponseEntity<BookDto> getBook(@PathVariable(value = "id") String id) {
        BookDto book = bookService.findById(id);
        return ResponseEntity.ok(book);
    }

    @PostMapping("/api/books")
    @CircuitBreaker(name = "bookServiceCircuitBreaker", fallbackMethod = "fallbackCreateBook")
    public ResponseEntity<BookDto> createBook(@Valid @RequestBody BookRequestDto book) {
        BookDto createdBook = bookService.insert(book);
        return ResponseEntity.ok(createdBook);
    }

    @PutMapping("/api/books/{id}")
    @CircuitBreaker(name = "bookServiceCircuitBreaker", fallbackMethod = "fallbackEditBook")
    public ResponseEntity<BookDto> editBook(@PathVariable(value = "id") String id,
                                            @Valid @RequestBody BookRequestDto book) {
        BookDto updatedBook = bookService.update(book);
        return ResponseEntity.ok(updatedBook);
    }

    @DeleteMapping("/api/books/{id}")
    @CircuitBreaker(name = "bookServiceCircuitBreaker", fallbackMethod = "fallbackRemoveBook")
    public ResponseEntity<String> removeBook(@PathVariable(value = "id") String id) {
        bookService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<List<BookDto>> fallbackGetBooks(Exception e) {
        log.error("Fallback triggered for getBooks. Error: ", e);
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(Collections.emptyList());
    }

    public ResponseEntity<BookDto> fallbackGetBook(String id, Exception e) {
        log.error("Fallback triggered for getBook with id {}. Error: ", id, e);
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(null);
    }

    public ResponseEntity<BookDto> fallbackCreateBook(BookRequestDto book, Exception e) {
        log.error("Fallback triggered for createBook. Error: ", e);
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(null);
    }

    public ResponseEntity<BookDto> fallbackEditBook(String id, BookRequestDto book, Exception e) {
        log.error("Fallback triggered for editBook with id {}. Error: ", id, e);
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(null);
    }

    public ResponseEntity<String> fallbackRemoveBook(String id, Exception e) {
        log.error("Fallback triggered for removeBook with id {}. Error: ", id, e);
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Failed to delete book");
    }
}
