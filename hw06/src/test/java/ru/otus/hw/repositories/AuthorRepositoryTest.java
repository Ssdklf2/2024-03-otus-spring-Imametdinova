package ru.otus.hw.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Author;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({AuthorRepositoryJpa.class})
public class AuthorRepositoryTest {

    private static final long FIRST_AUTHOR_ID = 1L;

    private static final int EXPECTED_NUMBER_OF_AUTHORS = 3;

    @Autowired
    private TestEntityManager em;

    @Autowired
    private AuthorRepositoryJpa authorRepository;

    @Test
    public void findByIdTest() {
        var optionalAuthor = authorRepository.findById(FIRST_AUTHOR_ID);
        var expectedAuthor = em.find(Author.class, FIRST_AUTHOR_ID);
        assertThat(optionalAuthor).isPresent().get()
                .usingRecursiveComparison().isEqualTo(expectedAuthor);
    }

    @Test
    public void findAllTest() {
        var authors = authorRepository.findAll();
        assertThat(authors).isNotNull().hasSize(EXPECTED_NUMBER_OF_AUTHORS);
    }
}
