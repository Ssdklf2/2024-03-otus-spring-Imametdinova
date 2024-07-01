package ru.otus.hw.services;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataMongoTest
@EnableConfigurationProperties
@Import({GenreServiceImpl.class})
class GenreServiceTest {

    @Autowired
    private GenreService genreService;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private BookRepository bookRepository;

    @Test
    void update() {
        var genre = new Genre();
        var id = "1";
        String anotherGenre = "another_genre";
        genre.setId(id);
        genre.setName(anotherGenre);

        genreService.update(genre);

        Genre actualGenre = genreRepository.findById(id).get();
        assertEquals(anotherGenre, actualGenre.getName());

        Book book = bookRepository.findById("1").get();
        assertEquals(anotherGenre, book.getGenre().getName());
    }
}