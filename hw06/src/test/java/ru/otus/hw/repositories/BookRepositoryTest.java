package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({BookRepositoryJpa.class, GenreRepositoryJpa.class})
public class BookRepositoryTest {

    private static final long FIRST_BOOK_ID = 1L;

    private static final int EXPECTED_NUMBER_OF_BOOKS = 3;

    @Autowired
    private BookRepositoryJpa bookRepositoryJpa;

    @Autowired
    private TestEntityManager em;

    @DisplayName("должен загружать книгу по id")
    @Test
    void shouldReturnCorrectBookById() {
        var optionalBook = bookRepositoryJpa.findById(FIRST_BOOK_ID);
        var expectedBook = em.find(Book.class, FIRST_BOOK_ID);
        assertThat(optionalBook).isPresent().get().usingRecursiveComparison().isEqualTo(expectedBook);
    }

    @DisplayName("должен загружать список всех книг")
    @Test
    void shouldReturnCorrectBooksList() {
        var actualBooks = bookRepositoryJpa.findAll();
        assertThat(actualBooks).isNotNull().hasSize(EXPECTED_NUMBER_OF_BOOKS);
    }

    @DisplayName("должен сохранять новую книгу")
    @Test
    void shouldSaveNewBook() {
        var newId = 4L;
        assertThat(em.find(Book.class, newId)).isNull();
        var book = new Book(0, "BookTitle_10500", new Author(), new Genre());
        bookRepositoryJpa.save(book);
        assertThat(em.find(Book.class, newId)).isNotNull();
    }

    @DisplayName("должен сохранять измененную книгу")
    @Test
    void shouldSaveUpdatedBook() {
        var book = new Book(FIRST_BOOK_ID, "BookTitle_10500", new Author(), new Genre());
        Book updatedBook = bookRepositoryJpa.save(book);
        Book expectedBook = em.find(Book.class, FIRST_BOOK_ID);
        assertThat(updatedBook).usingRecursiveComparison().isEqualTo(expectedBook);
    }

    @DisplayName("должен удалять книгу по id ")
    @Test
    void shouldDeleteBook() {
        bookRepositoryJpa.deleteById(FIRST_BOOK_ID);
        assertThat(em.find(Book.class, FIRST_BOOK_ID)).isNull();
    }
}
