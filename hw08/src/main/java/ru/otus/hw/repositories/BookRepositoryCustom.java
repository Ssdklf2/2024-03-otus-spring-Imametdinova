package ru.otus.hw.repositories;

import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

public interface BookRepositoryCustom {

    void deleteBook(Book book);

    void deleteAll();

    long updateAuthor(Author author);

    long updateGenre(Genre genre);
}
