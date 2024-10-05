package ru.otus.hw.mongo.changelogs;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import org.bson.types.ObjectId;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.MongoAuthorRepository;
import ru.otus.hw.repositories.MongoBookRepository;
import ru.otus.hw.repositories.MongoGenreRepository;

import java.util.List;

@ChangeLog
public class DatabaseChangelog {

    private List<Genre> genres;

    private List<Author> authors;

    private List<Book> books;

    @ChangeSet(order = "001", id = "dropDb", author = "", runAlways = true)
    public void dropDb(MongoDatabase db) {
        db.drop();
    }

    @ChangeSet(order = "002", id = "insertAuthors", author = "", runAlways = true)
    public void insertAuthors(MongoAuthorRepository repository) {
        authors = repository.saveAll(List.of(
                new Author(new ObjectId().toString(), "Author_1"),
                new Author(new ObjectId().toString(), "Author_2"),
                new Author(new ObjectId().toString(), "Author_3"),
                new Author(new ObjectId().toString(), "Author_4"),
                new Author(new ObjectId().toString(), "Author_5"),
                new Author(new ObjectId().toString(), "Author_6"),
                new Author(new ObjectId().toString(), "Author_7")));
    }

    @ChangeSet(order = "003", id = "insertGenres", author = "", runAlways = true)
    public void insertGenres(MongoGenreRepository repository) {
        genres = repository.saveAll(List.of(
                new Genre(new ObjectId().toString(), "Genre_1"),
                new Genre(new ObjectId().toString(), "Genre_2"),
                new Genre(new ObjectId().toString(), "Genre_3"),
                new Genre(new ObjectId().toString(), "Genre_4"),
                new Genre(new ObjectId().toString(), "Genre_5"),
                new Genre(new ObjectId().toString(), "Genre_6"),
                new Genre(new ObjectId().toString(), "Genre_7")));
    }


    @ChangeSet(order = "004", id = "insertBooks", author = "", runAlways = true)
    public void insertBooks(MongoBookRepository repository) {
        books = repository.saveAll(List.of(
                new Book(new ObjectId().toString(), "BookTitle_1", authors.get(0), genres.get(0)),
                new Book(new ObjectId().toString(), "BookTitle_2", authors.get(1), genres.get(1)),
                new Book(new ObjectId().toString(), "BookTitle_3", authors.get(2), genres.get(2)),
                new Book(new ObjectId().toString(), "BookTitle_4", authors.get(3), genres.get(3)),
                new Book(new ObjectId().toString(), "BookTitle_5", authors.get(4), genres.get(4)),
                new Book(new ObjectId().toString(), "BookTitle_6", authors.get(5), genres.get(5)),
                new Book(new ObjectId().toString(), "BookTitle_7", authors.get(6), genres.get(6))));
    }
}