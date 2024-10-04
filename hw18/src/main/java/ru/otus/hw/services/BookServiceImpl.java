package ru.otus.hw.services;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookRequestDto;
import ru.otus.hw.exceptions.NotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    private final AnotherBookSourceService bookSourceService;

    @CircuitBreaker(name = "bookServiceCircuitBreaker", fallbackMethod = "fallbackFindById")
    @Override
    public BookDto findById(String id) {
        var book = bookRepository.findById(id).orElseThrow(() -> new NotFoundException("book not found"));
        return BookDto.fromDomainObject(book);
    }

    @CircuitBreaker(name = "bookServiceCircuitBreaker", fallbackMethod = "fallbackFindAll")
    @Transactional(readOnly = true)
    @Override
    public List<BookDto> findAll() {
        return bookRepository.findAll().stream().map((BookDto::fromDomainObject)).toList();
    }

    @CircuitBreaker(name = "bookServiceCircuitBreaker", fallbackMethod = "fallbackInsert")
    @Transactional
    @Override
    public BookDto insert(BookRequestDto bookDto) {
        Book savedBook = save(null, bookDto.getTitle(), bookDto.getAuthorId(), bookDto.getGenreId());
        return BookDto.fromDomainObject(savedBook);
    }

    @CircuitBreaker(name = "bookServiceCircuitBreaker", fallbackMethod = "fallbackUpdate")
    @Transactional
    @Override
    public BookDto update(BookRequestDto bookDto) {
        Book savedBook = save(bookDto.getId(), bookDto.getTitle(), bookDto.getAuthorId(), bookDto.getGenreId());
        return BookDto.fromDomainObject(savedBook);
    }

    @CircuitBreaker(name = "bookServiceCircuitBreaker", fallbackMethod = "fallbackDeleteById")
    @Transactional
    @Override
    public void deleteById(String id) {
        bookRepository.deleteByBookId(id);
    }

    public BookDto  fallbackFindById(String id) {
        return bookSourceService.findById(id);
    }

    public List<BookDto> fallbackFindAll() {
        return bookSourceService.findAll();
    }

    public BookDto fallbackInsert(BookRequestDto bookDto) {
        return bookSourceService.insert(bookDto);
    }

    public BookDto fallbackUpdate(BookRequestDto bookDto) {
        return bookSourceService.update(bookDto);
    }

    public void fallbackDeleteById(String id) {
        bookSourceService.deleteById(id);
    }

    private Book save(String id, String title, String authorId, String genreId) {
        var author = authorRepository.findById(authorId)
                .orElseThrow(() -> new NotFoundException("Author with id %s not found".formatted(authorId)));
        var genre = genreRepository.findById(genreId)
                .orElseThrow(() -> new NotFoundException("Genre with id %s not found".formatted(genreId)));
        var book = new Book(id, title, author, genre);
        return bookRepository.save(book);
    }
}
