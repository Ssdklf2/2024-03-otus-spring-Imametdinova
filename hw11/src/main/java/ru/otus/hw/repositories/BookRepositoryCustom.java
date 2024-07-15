package ru.otus.hw.repositories;


import reactor.core.publisher.Mono;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Genre;

public interface BookRepositoryCustom {

    Mono<Void> deleteByBookId(String bookId);

    Mono<Void> deleteAll();

    long updateAuthor(Author author);

    long updateGenre(Genre genre);
}
