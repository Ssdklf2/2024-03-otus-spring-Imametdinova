package ru.otus.hw.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookRequestDto;
import ru.otus.hw.exceptions.NotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

@RestController
@RequiredArgsConstructor
public class BookController {

    private final BookRepository bookRepository;

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    @GetMapping(value = "/api/books", produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<BookDto> getBooks() {
        return bookRepository.findAll().map(BookDto::fromDomainObject);
    }

    @GetMapping(value = "/api/books/{id}")
    public Mono<BookDto> getBook(@PathVariable(value = "id") String id) {
        return bookRepository.findById(id)
                .map(BookDto::fromDomainObject)
                .switchIfEmpty(Mono.error(new NotFoundException("Book with id %s not found".formatted(id))));
    }

    @PostMapping("/api/books")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<BookDto> createBook(@Valid @RequestBody BookRequestDto bookDto) {
        return save(null, bookDto.getTitle(), bookDto.getAuthorId(), bookDto.getGenreId())
                .map(BookDto::fromDomainObject);
    }

    @PutMapping("/api/books/{id}")
    public Mono<BookDto> editBook(@PathVariable(value = "id") String id,
                                  @Valid @RequestBody BookRequestDto bookDto) {
        return save(bookDto.getId(), bookDto.getTitle(), bookDto.getAuthorId(), bookDto.getGenreId())
                .map(BookDto::fromDomainObject);
    }

    @DeleteMapping("/api/books/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public Mono<Void> removeBook(@PathVariable(value = "id") String id) {
        return bookRepository.deleteById(id);
    }

    private Mono<Book> save(String id, String title, String authorId, String genreId) {
        return authorRepository.findById(authorId)
                .switchIfEmpty(Mono.error(new NotFoundException("Author with id %s not found".formatted(authorId))))
                .zipWith(genreRepository.findById(genreId))
                .switchIfEmpty(Mono.error(new NotFoundException("Genre with id %s not found".formatted(genreId))))
                .flatMap(tuple -> bookRepository.save(new Book(id, title, tuple.getT1(), tuple.getT2())));
    }
}
