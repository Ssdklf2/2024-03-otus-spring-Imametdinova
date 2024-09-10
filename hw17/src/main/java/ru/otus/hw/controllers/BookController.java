package ru.otus.hw.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping("/api/books")
    public ResponseEntity<List<BookDto>> getBooks() {
        List<BookDto> books = bookService.findAll();
        return ResponseEntity.ok(books);
    }

    @GetMapping("/api/books/{id}")
    public ResponseEntity<BookDto> getBook(@PathVariable(value = "id") String id) {
        BookDto book = bookService.findById(id);
        return ResponseEntity.ok(book);
    }

    @PostMapping("/api/books")
    public ResponseEntity<BookDto> createBook(@Valid @RequestBody BookRequestDto book) {
        BookDto createdBook = bookService.insert(book);
        return ResponseEntity.ok(createdBook);
    }

    @PutMapping("/api/books/{id}")
    public ResponseEntity<BookDto> editBook(@PathVariable(value = "id") String id,
                                            @Valid @RequestBody BookRequestDto book) {
        BookDto updatedBook = bookService.update(book);
        return ResponseEntity.ok(updatedBook);
    }

    @DeleteMapping("/api/books/{id}")
    public ResponseEntity<String> removeBook(@PathVariable(value = "id") String id) {
        bookService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
