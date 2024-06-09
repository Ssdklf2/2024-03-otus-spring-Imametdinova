package ru.otus.hw.repositories;

import com.mongodb.client.result.UpdateResult;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class BookRepositoryCustomImpl implements BookRepositoryCustom {

    private final MongoTemplate mongoTemplate;

    private final CommentRepository commentRepository;

    @Override
    public void deleteBook(Book book) {
        mongoTemplate.remove(Query.query(Criteria.where("_id").is(book.getId())), Book.class);
        List<Comment> comments = commentRepository.findByBookId(book.getId());
        commentRepository.deleteAllById(comments.stream().map(Comment::getId).collect(Collectors.toSet()));
    }

    @Override
    public void deleteAll() {
        mongoTemplate.remove(new Query(), Book.class);
        commentRepository.deleteAll();
    }

    @Override
    public long updateAuthor(Author author) {
        Query query = Query.query(Criteria.where("author._id").is(author.getId()));
        Update update = new Update().set("author", author);
        UpdateResult updateResult = mongoTemplate.updateMulti(query, update, Book.class);
        return updateResult.getMatchedCount();
    }

    @Override
    public long updateGenre(Genre genre) {
        Query query = Query.query(Criteria.where("genre._id").is(genre.getId()));
        Update update = new Update().set("genre", genre);
        UpdateResult updateResult = mongoTemplate.updateMulti(query, update, Book.class);
        return updateResult.getMatchedCount();
    }
}