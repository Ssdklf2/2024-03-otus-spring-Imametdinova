package ru.otus.hw.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@DataMongoTest
@EnableConfigurationProperties
@ComponentScan({"ru.otus.hw.repositories"})
public class BookRepositoryCustomTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void deleteByIdTest() {
        Book book = new Book();
        book.setId(1);

        bookRepository.deleteBook(book);
        List<Comment> comments = commentRepository.findByBookId(1);
        assertTrue(comments.isEmpty());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void deleteAllTest() {
        bookRepository.deleteAll();
        List<Comment> comments = commentRepository.findByBookId(1);
        assertTrue(comments.isEmpty());
    }

    @Test
    void updateAuthorTest() {
        var author = new Author();
        var anotherName = "another_name";
        author.setId(1L);
        author.setFullName(anotherName);

        assertEquals(1, bookRepository.updateAuthor(author));

        Book book = bookRepository.findById(1L).get();
        assertEquals(anotherName, book.getAuthor().getFullName());
    }

    @Test
    void updateGenreTest() {
        var genre = new Genre();
        String anotherName = "another_genre";
        genre.setId(1L);
        genre.setName(anotherName);

        assertEquals(1, bookRepository.updateGenre(genre));

        Book book = bookRepository.findById(1L).get();
        assertEquals(anotherName, book.getGenre().getName());
    }
}
