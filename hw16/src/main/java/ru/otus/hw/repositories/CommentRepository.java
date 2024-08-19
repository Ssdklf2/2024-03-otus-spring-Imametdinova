package ru.otus.hw.repositories;


import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import ru.otus.hw.models.Comment;

import java.util.List;

public interface CommentRepository extends ReactiveMongoRepository<Comment, String> {

    List<Comment> findByBookId(String bookId);

}
