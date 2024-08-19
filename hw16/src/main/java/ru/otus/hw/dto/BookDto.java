package ru.otus.hw.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.hw.models.Book;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDto {

    private String id;

    @NotBlank(message = "{title-should-not-be-blank}")
    private String title;

    private AuthorDto author;

    private GenreDto genre;

    public BookDto(String title, AuthorDto author, GenreDto genre) {
        this.title = title;
        this.author = author;
        this.genre = genre;
    }

    public Book toDomainObject() {
        return new Book(id, title, author.toDomainObject(), genre.toDomainObject());
    }

    public static BookDto fromDomainObject(Book book) {
        return new BookDto(book.getId(), book.getTitle(), AuthorDto.fromDomainObject(book.getAuthor()),
                GenreDto.fromDomainObject(book.getGenre()));
    }
}
