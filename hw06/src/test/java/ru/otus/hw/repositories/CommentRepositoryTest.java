package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({CommentRepositoryJpa.class})
public class CommentRepositoryTest {

    private static final long FIRST_COMMENT_ID = 1L;

    private static final int EXPECTED_NUMBER_OF_COMMENTS = 3;

    @Autowired
    private CommentRepositoryJpa commentRepositoryJpa;

    @Autowired
    private TestEntityManager em;

    @DisplayName("должен загружать комментарий по id")
    @Test
    void shouldReturnCorrectCommentById() {
        var optionalComment = commentRepositoryJpa.findById(FIRST_COMMENT_ID);
        var expectedComment = em.find(Comment.class, FIRST_COMMENT_ID);
        assertThat(optionalComment).isPresent().get().usingRecursiveComparison().isEqualTo(expectedComment);
    }

    @DisplayName("должен загружать список всех комментариев по id книги")
    @Test
    void shouldReturnCorrectCommentsList() {
        var bookId = 1L;
        var actualComments = commentRepositoryJpa.findByBookId(bookId);
        assertThat(actualComments).isNotNull().hasSize(2);
    }

    @DisplayName("должен сохранять новый комментарий")
    @Test
    void shouldSaveNewComment() {
        var newId = 4L;
        assertThat(em.find(Comment.class, newId)).isNull();
        var comment = new Comment(0, "comm1", new Book());
        commentRepositoryJpa.save(comment);
        assertThat(em.find(Comment.class, newId)).isNotNull();
    }

    @DisplayName("должен сохранять измененный комментарий")
    @Test
    void shouldSaveUpdatedComment() {
        var book = new Book();
        book.setId(1L);
        var comment = new Comment(3, "comm1", book);
        var updatedComment = commentRepositoryJpa.save(comment);
        var expectedComment = em.find(Comment.class, 3);
        assertThat(updatedComment).usingRecursiveComparison().isEqualTo(expectedComment);
    }

    @DisplayName("должен удалять комментарий по id ")
    @Test
    void shouldDeleteComment() {
        commentRepositoryJpa.deleteById(FIRST_COMMENT_ID);
        assertThat(em.find(Comment.class, FIRST_COMMENT_ID)).isNull();
    }
}
