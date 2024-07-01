package ru.otus.hw.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.hw.repositories.BookRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataMongoTest
@EnableConfigurationProperties
@Import({BookServiceImpl.class})
public class BookServiceTest {

    private static final String FIRST_BOOK_ID = "1";

    private static final int EXPECTED_NUMBER_OF_BOOKS = 3;

    @Autowired
    private BookService bookService;

    @Autowired
    private BookRepository bookRepository;

    @Test
    public void findByIdTest() {
        var book = bookService.findById(FIRST_BOOK_ID);
        var expectedBook = bookRepository.findById(FIRST_BOOK_ID);
        assertThat(book).isPresent().usingRecursiveComparison().isEqualTo(expectedBook);
    }

    @Test
    public void findAllTest() {
        var actualBooks = bookService.findAll();
        assertThat(actualBooks).isNotNull().hasSize(EXPECTED_NUMBER_OF_BOOKS);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void insertTest() {
        long beforeInsertCount = bookRepository.count();
        bookService.insert("title", "1", "1");
        assertEquals(++beforeInsertCount, bookRepository.count());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void deleteByIdTest() {
        bookService.deleteById(FIRST_BOOK_ID);
        assertTrue(bookRepository.findById(FIRST_BOOK_ID).isEmpty());
    }

    @Test
    public void updateTest() {
        var id = "3";
        var updatedBook = bookService.update(id, "title", "1", "1");
        var expectedBook = bookRepository.findById(id);
        assertTrue(expectedBook.isPresent());
        assertEquals(expectedBook.get(), updatedBook);
    }
}
