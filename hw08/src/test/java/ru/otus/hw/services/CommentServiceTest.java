package ru.otus.hw.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.hw.repositories.CommentRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataMongoTest
@EnableConfigurationProperties
@Import({CommentServiceImpl.class})
class CommentServiceTest {

    private static final String FIRST_COMMENT_ID = "1";

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
        var bookId = "1";
        var actualComments = commentService.findAllByBookId(bookId);
        assertThat(actualComments).isNotNull().hasSize(1);
        assertEquals("text_1", actualComments.get(0).getText());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void insertTest() {
        long beforeInsertCount = commentRepository.count();
        commentService.insert("text", "1");
        assertEquals(++beforeInsertCount, commentRepository.count());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void deleteByIdTest() {
        commentService.deleteById(FIRST_COMMENT_ID);
        assertTrue(commentRepository.findById(FIRST_COMMENT_ID).isEmpty());
    }

    @Test
    public void updateTest() {
        var id = "3";
        var updatedComment = commentService.update(id, "text1", "1");
        var expectedComment = commentRepository.findById(id);
        assertTrue(expectedComment.isPresent());
        assertEquals(expectedComment.get(), updatedComment);
    }
}