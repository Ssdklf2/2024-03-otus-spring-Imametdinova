package ru.otus.hw.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Genre;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({GenreRepositoryJpa.class})
class GenreRepositoryTest {

    private static final long FIRST_GENRE_ID = 1L;

    private static final int EXPECTED_NUMBER_OF_GENRES = 3;

    @Autowired
    private TestEntityManager em;

    @Autowired
    private GenreRepositoryJpa genreRepository;

    @Test
    public void findByIdTest() {
        var optionalGenre = genreRepository.findById(FIRST_GENRE_ID);
        var expectedGenre = em.find(Genre.class, FIRST_GENRE_ID);
        assertThat(optionalGenre).isPresent().get()
                .usingRecursiveComparison().isEqualTo(expectedGenre);
    }

    @Test
    public void findAllTest() {
        var genres = genreRepository.findAll();
        assertThat(genres).isNotNull().hasSize(EXPECTED_NUMBER_OF_GENRES);
    }
}