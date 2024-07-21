package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import reactor.core.publisher.Mono;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;


@RequiredArgsConstructor
public class BookRepositoryCustomImpl implements BookRepositoryCustom {

    private final ReactiveMongoTemplate mongoTemplate;

    private final CommentRepository commentRepository;

    @Override
    public Mono<Void> deleteByBookId(String bookId) {
        return mongoTemplate.remove(Query.query(Criteria.where("_id").is(bookId)), Book.class)
                .then(mongoTemplate.remove(Query.query(Criteria.where("book._id").is(bookId)), Comment.class))
                .then();
    }

    @Override
    public Mono<Void> deleteAll() {
        return mongoTemplate.remove(new Query(), Book.class)
                .then(commentRepository.deleteAll())
                .then();
    }

    @Override
    public Mono<Void> updateAuthor(Author author) {
        Query query = Query.query(Criteria.where("author._id").is(author.getId()));
        Update update = new Update().set("author", author);
        return mongoTemplate.updateMulti(query, update, Book.class).then();
    }

    @Override
    public Mono<Void> updateGenre(Genre genre) {
        Query query = Query.query(Criteria.where("genre._id").is(genre.getId()));
        Update update = new Update().set("genre", genre);
        return mongoTemplate.updateMulti(query, update, Book.class).then();
    }
}