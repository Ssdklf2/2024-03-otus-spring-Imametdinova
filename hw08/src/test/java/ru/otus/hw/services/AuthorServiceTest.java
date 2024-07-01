package ru.otus.hw.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataMongoTest
@EnableConfigurationProperties
@Import({AuthorServiceImpl.class})
class AuthorServiceTest {

    @Autowired
    private AuthorService authorService;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookRepository bookRepository;

    @Test
    void update() {
        var author = new Author();
        var id = "1";
        String anotherName = "another_name";
        author.setId(id);
        author.setFullName(anotherName);

        authorService.update(author);

        Author actualAuthor = authorRepository.findById(id).get();
        assertEquals(anotherName, actualAuthor.getFullName());

        Book book = bookRepository.findById("1").get();
        assertEquals(anotherName, book.getAuthor().getFullName());
    }
}