package ru.otus.hw.repositories;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class JdbcBookRepository implements BookRepository {

    private final NamedParameterJdbcOperations jdbcOperations;

    public JdbcBookRepository(NamedParameterJdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }

    @Override
    public Optional<Book> findById(long id) {
        Map<String, Object> params = Collections.singletonMap("id", id);
        List<Book> result = jdbcOperations.query(
                "select b.id, title, author_id, full_name, genre_id, name from books b" +
                        " inner join authors a on author_id = a.id" +
                        " inner join genres g on genre_id = g.id" +
                        " where b.id = :id",
                params, new BookRowMapper());
        if (result.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(result.get(0));
    }

    @Override
    public List<Book> findAll() {
        return jdbcOperations.query(
                "select b.id, title, author_id, full_name, genre_id, name from books b" +
                        " inner join authors a on author_id = a.id" +
                        " inner join genres g on genre_id = g.id",
                new BookRowMapper());
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            return insert(book);
        }
        return update(book);
    }

    @Override
    public void deleteById(long id) {
        Map<String, Object> params = Collections.singletonMap("id", id);
        jdbcOperations.update("delete from books where id = :id",
                params);
    }

    private Book insert(Book book) {
        var keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("title", book.getTitle());
        params.addValue("author_id", book.getAuthor().getId());
        params.addValue("genre_id", book.getGenre().getId());


        jdbcOperations.update("insert into books(title, author_id, genre_id) values (:title, :author_id, :genre_id)",
                params, keyHolder, new String[]{"id"});

        //noinspection DataFlowIssue
        book.setId(keyHolder.getKeyAs(Long.class));

        return book;
    }

    private Book update(Book book) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", book.getId());
        params.addValue("title", book.getTitle());
        params.addValue("author_id", book.getAuthor().getId());
        params.addValue("genre_id", book.getGenre().getId());


        int update = jdbcOperations.update(
                "update books set title = :title, author_id = :author_id, genre_id = :genre_id" +
                        " where id = :id", params);

        if (update == 0) {
            throw new EntityNotFoundException("Book not found by id " + book.getId());
        }
        return book;
    }

    private static class BookRowMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            long authorId = rs.getLong("author_id");
            String fullName = rs.getString("full_name");
            Author author = new Author(authorId, fullName);

            long genreId = rs.getLong("genre_id");
            String name = rs.getString("name");
            Genre genre = new Genre(genreId, name);

            long id = rs.getLong("id");
            String title = rs.getString("title");

            return new Book(id, title, author, genre);
        }
    }
}
