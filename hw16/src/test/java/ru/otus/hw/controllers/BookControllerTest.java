package ru.otus.hw.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookRequestDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;
import java.util.Objects;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@WebFluxTest(BookController.class)
class BookControllerTest {

    @Autowired
    private WebTestClient client;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private AuthorRepository authorRepository;

    @MockBean
    private GenreRepository genreRepository;

    private List<Genre> genres;

    private List<Author> authors;

    private List<BookDto> books;

    @BeforeEach
    public void init() {
        genres = getGenres();
        authors = getAuthors();
        books = getBooksDto();
    }

    @Test
    void getBooks() {
        Flux<Book> bookFlux = Flux.fromIterable(books.stream().map(BookDto::toDomainObject).toList());
        given(bookRepository.findAll()).willReturn(bookFlux);
        var result = client.get().uri("/api/books")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(BookDto.class)
                .getResponseBody();
        var step = StepVerifier.create(result);
        StepVerifier.Step<BookDto> stepResult = null;
        for (BookDto book : books) {
            stepResult = step.expectNext(book);
        }
        Objects.requireNonNull(stepResult).verifyComplete();
    }

    @Test
    void getBook() {
        String id = "1";
        BookDto bookDto = books.get(0);
        given(bookRepository.findById(id)).willReturn(Mono.just(bookDto.toDomainObject()));

        var result = client.get().uri("/api/books/" + id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(BookDto.class)
                .getResponseBody();

        var step = StepVerifier.create(result);
        StepVerifier.Step<BookDto> stepResult = step.expectNext(bookDto);
        Objects.requireNonNull(stepResult).verifyComplete();
    }

    @Test
    void createBook() throws Exception {
        BookRequestDto request = new BookRequestDto(null, "title", "a1", "g1");
        given(bookRepository.save(any()))
                .willReturn(Mono.just(books.get(0).toDomainObject()));
        given(authorRepository.findById(anyString())).willReturn(Mono.just(authors.get(0)));
        given(genreRepository.findById(anyString())).willReturn(Mono.just(genres.get(1)));

        var result = client.post().uri("/api/books")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .returnResult(BookDto.class)
                .getResponseBody();

        var step = StepVerifier.create(result);
        StepVerifier.Step<BookDto> stepResult = step.expectNext(books.get(0));
        Objects.requireNonNull(stepResult).verifyComplete();
    }

    @Test
    void editBook() throws Exception {
        String id = "1";
        BookRequestDto request = new BookRequestDto(id, "title", "a1", "g1");

        given(bookRepository.save(any())).willReturn(Mono.just(books.get(2).toDomainObject()));
        given(authorRepository.findById(anyString())).willReturn(Mono.just(authors.get(0)));
        given(genreRepository.findById(anyString())).willReturn(Mono.just(genres.get(1)));

        var result = client.put().uri("/api/books/" + id)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .returnResult(BookDto.class)
                .getResponseBody();

        var step = StepVerifier.create(result);
        StepVerifier.Step<BookDto> stepResult = step.expectNext(books.get(2));
        Objects.requireNonNull(stepResult).verifyComplete();

    }

    @Test
    void removeBook() throws Exception {
        String id = "1";
        client.delete().uri("/api/books/" + id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNoContent();
    }

    private List<Author> getAuthors() {
        return List.of(new Author("1", "Author_1"), new Author("2", "Author_2"), new Author("3", "Author_3"));
    }

    private List<Genre> getGenres() {
        return List.of(new Genre("1", "Genre_1"), new Genre("2", "Genre_2"), new Genre("3", "Genre_3"));
    }


    private List<BookDto> getBooksDto() {
        return List.of(
                new BookDto("1", "BookTitle_1", AuthorDto.fromDomainObject(authors.get(0)), GenreDto.fromDomainObject(genres.get(0))),
                new BookDto("2", "BookTitle_2", AuthorDto.fromDomainObject(authors.get(1)), GenreDto.fromDomainObject(genres.get(1))),
                new BookDto("3", "BookTitle_3", AuthorDto.fromDomainObject(authors.get(2)), GenreDto.fromDomainObject(genres.get(2))));
    }
}