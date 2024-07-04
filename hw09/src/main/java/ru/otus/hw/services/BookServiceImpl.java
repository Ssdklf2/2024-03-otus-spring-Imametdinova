package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
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

    @Override
    public BookDto findById(String id) {
        var book = bookRepository.findById(id).orElseThrow(NotFoundException::new);
        return BookDto.fromDomainObject(book);
    }

    @Transactional(readOnly = true)
    @Override
    public List<BookDto> findAll() {
        return bookRepository.findAll().stream().map((BookDto::fromDomainObject)).toList();
    }

    @Transactional
    @Override
    public BookDto insert(BookDto bookDto) {
        Book book = bookDto.toDomainObject();
        Book savedBook = save(null, book.getTitle(), book.getAuthor().getId(), book.getGenre().getId());
        return BookDto.fromDomainObject(savedBook);
    }

    @Transactional
    @Override
    public BookDto update(BookDto bookDto) {
        Book book = bookDto.toDomainObject();
        Book savedBook = save(book.getId(), book.getTitle(), book.getAuthor().getId(), book.getGenre().getId());
        return BookDto.fromDomainObject(savedBook);
    }

    @Transactional
    @Override
    public void deleteById(String id) {
        bookRepository.deleteByBookId(id);
    }

    private Book save(String id, String title, String authorId, String genreId) {
        var author = authorRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("Author with id %d not found".formatted(authorId)));
        var genre = genreRepository.findById(genreId)
                .orElseThrow(() -> new EntityNotFoundException("Genre with id %d not found".formatted(genreId)));
        var book = new Book(id, title, author, genre);
        return bookRepository.save(book);
    }
}
