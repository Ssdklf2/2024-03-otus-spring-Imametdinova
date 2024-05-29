package ru.otus.hw.services;

import jakarta.persistence.NoResultException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.repositories.BookRepositoryJpa;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.repositories.CommentRepositoryJpa;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({CommentServiceImpl.class, CommentRepositoryJpa.class, BookRepositoryJpa.class})
@Transactional(propagation = Propagation.NOT_SUPPORTED)
class CommentServiceTest {

    private static final long FIRST_COMMENT_ID = 1L;

    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentRepository commentRepository;

    @Test
    public void findByIdTest() {
        var comment = commentService.findById(FIRST_COMMENT_ID);
        var expectedComment = commentRepository.findById(FIRST_COMMENT_ID);
        assertTrue(comment.isPresent());
        assertTrue(expectedComment.isPresent());
        assertEquals(expectedComment.get(), comment.get());
    }

    @Test
    public void findAllByBookIdTest() {
        var bookId = 1L;
        var actualComments = commentService.findAllByBookId(bookId);
        assertThat(actualComments).isNotNull().hasSize(2);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void insertTest() {
        long expectedId = 4L;
        commentService.insert("text", 1L);
        var expectedComment = commentRepository.findById(expectedId);
        assertTrue(expectedComment.isPresent());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void deleteByIdTest() {
        commentService.deleteById(FIRST_COMMENT_ID);
        assertTrue(commentRepository.findById(FIRST_COMMENT_ID).isEmpty());
    }

    @Test
    public void updateTest() {
        long id = 3L;
        var updatedComment = commentService.update(id, "text1", 1L);
        var expectedComment = commentRepository.findById(id);
        assertTrue(expectedComment.isPresent());
        assertEquals(expectedComment.get(), updatedComment);
    }
}