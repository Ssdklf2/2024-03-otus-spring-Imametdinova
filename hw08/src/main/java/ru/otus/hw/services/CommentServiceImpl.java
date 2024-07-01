package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    @Override
    public List<Comment> findAllByBookId(String bookId) {
        return commentRepository.findByBookId(bookId);
    }

    @Override
    public Optional<Comment> findById(String id) {
        return commentRepository.findById(id);
    }

    @Transactional
    @Override
    public void deleteById(String id) {
        commentRepository.deleteById(id);
    }

    @Transactional
    @Override
    public Comment update(String id, String text, String bookId) {
        return save(id, text, bookId);
    }

    @Transactional
    @Override
    public Comment insert(String text, String bookId) {
        return save(null, text, bookId);
    }

    private Comment save(String id, String text, String bookId) {
        var author = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %d not found".formatted(bookId)));
        var comment = new Comment(id, text, author);
        return commentRepository.save(comment);
    }
}
