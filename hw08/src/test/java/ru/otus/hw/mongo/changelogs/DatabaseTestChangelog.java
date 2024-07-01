package ru.otus.hw.mongo.changelogs;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;

import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;

@ChangeLog
public class DatabaseTestChangelog {

    List<Genre> genres;

    List<Author> authors;

    List<Book> books;

    @ChangeSet(order = "001", id = "dropDb", author = "", runAlways = true)
    public void dropDb(MongoDatabase db) {
        db.drop();
    }

    @ChangeSet(order = "002", id = "insertAuthors", author = "", runAlways = true)
    public void insertAuthors(AuthorRepository repository) {
        authors = repository.saveAll(List.of(
                new Author("1", "Author_1"),
                new Author("2", "Author_2"),
                new Author("3", "Author_3")));
    }

    @ChangeSet(order = "003", id = "insertGenres", author = "", runAlways = true)
    public void insertGenres(GenreRepository repository) {
        genres = repository.saveAll(List.of(
                new Genre("1", "Genre_1"),
                new Genre("2", "Genre_2"),
                new Genre("3", "Genre_3")));
    }


    @ChangeSet(order = "004", id = "insertBooks", author = "", runAlways = true)
    public void insertBooks(BookRepository repository) {
        books = repository.saveAll(List.of(
                new Book("1", "BookTitle_1", authors.get(0), genres.get(0)),
                new Book("2", "BookTitle_2", authors.get(1), genres.get(1)),
                new Book("3", "BookTitle_3", authors.get(2), genres.get(2))));
    }


    @ChangeSet(order = "005", id = "insertComments", author = "", runAlways = true)
    public void insertComments(CommentRepository repository) {
        repository.saveAll(List.of(
                new Comment("1", "text_1", books.get(0)),
                new Comment("2", "text_2", books.get(1)),
                new Comment("3", "text_3", books.get(2))));
    }
}