package ru.otus.hw.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.repositories.AuthorRepositoryJpa;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.BookRepositoryJpa;
import ru.otus.hw.repositories.GenreRepositoryJpa;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Import({BookServiceImpl.class, AuthorRepositoryJpa.class, GenreRepositoryJpa.class, BookRepositoryJpa.class})
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class BookServiceTest {

    private static final long FIRST_BOOK_ID = 1L;

    private static final int EXPECTED_NUMBER_OF_BOOKS = 3;

    @Autowired
    private BookService service;

    @Autowired
    private BookRepository bookRepository;

    @Test
    public void findByIdTest() {
        var book = service.findById(FIRST_BOOK_ID);
        var expectedBook = bookRepository.findById(FIRST_BOOK_ID);
        assertThat(book).isPresent().usingRecursiveComparison().isEqualTo(expectedBook);
    }

    @Test
    public void findAllTest() {
        var actualBooks = service.findAll();
        assertThat(actualBooks).isNotNull().hasSize(EXPECTED_NUMBER_OF_BOOKS);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void insertTest() {
        var expectedId = 4L;
        service.insert("title", 1L, 1L);
        var expectedBook = bookRepository.findById(expectedId);
        assertTrue(expectedBook.isPresent());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void deleteByIdTest() {
        service.deleteById(FIRST_BOOK_ID);
        assertTrue(bookRepository.findById(FIRST_BOOK_ID).isEmpty());
    }

    @Test
    public void updateTest() {
        var id = 3L;
        var updatedBook = service.update(id, "title", 1L, 1L);
        var expectedBook = bookRepository.findById(id);
        assertTrue(expectedBook.isPresent());
        assertEquals(expectedBook.get(), updatedBook);
    }
}
