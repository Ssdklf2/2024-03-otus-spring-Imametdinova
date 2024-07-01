package ru.otus.hw.services;


import ru.otus.hw.models.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    List<Comment> findAllByBookId(String bookId);

    Optional<Comment> findById(String id);

    void deleteById(String id);

    Comment update(String id, String text, String bookId);

    Comment insert(String text, String bookId);
}
