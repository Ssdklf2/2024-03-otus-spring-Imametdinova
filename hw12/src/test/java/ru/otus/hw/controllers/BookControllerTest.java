package ru.otus.hw.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookRequestDto;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Genre;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CommentService;
import ru.otus.hw.services.GenreService;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
class BookControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookService bookService;

    @MockBean
    private CommentService commentService;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private GenreService genreService;

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
    void getBooks() throws Exception {
        given(bookService.findAll()).willReturn(books);
        mvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(books)));
    }

    @Test
    void getBook() throws Exception {
        String id = "1";
        given(bookService.findById(id)).willReturn(books.get(0));
        mvc.perform(get("/api/books/" + id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value("BookTitle_1"));
    }

    @Test
    void createBook() throws Exception {
        BookRequestDto request = new BookRequestDto(null, "title", "a1", "g1");
        given(bookService.insert(request)).willReturn(books.get(1));
        mvc.perform(MockMvcRequestBuilders
                        .post("/api/books")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("BookTitle_2"));
    }

    @Test
    void editBook() throws Exception {
        String id = "1";
        BookRequestDto request = new BookRequestDto(id, "title", "a1", "g1");
        given(bookService.update(request)).willReturn(books.get(2));
        mvc.perform(MockMvcRequestBuilders
                        .put("/api/books/" + id)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("BookTitle_3"));
    }

    @Test
    void removeBook() throws Exception {
        String id = "1";
        mvc.perform(delete("/api/books/" + id))
                .andExpect(status().isNoContent());
    }

    private List<Author> getAuthors() {
        return List.of(
                new Author("1", "Author_1"),
                new Author("2", "Author_2"),
                new Author("3", "Author_3"));
    }

    private List<Genre> getGenres() {
        return List.of(
                new Genre("1", "Genre_1"),
                new Genre("2", "Genre_2"),
                new Genre("3", "Genre_3"));
    }


    private List<BookDto> getBooksDto() {
        return List.of(
                new BookDto("1", "BookTitle_1", authors.get(0), genres.get(0)),
                new BookDto("2", "BookTitle_2", authors.get(1), genres.get(1)),
                new BookDto("3", "BookTitle_3", authors.get(2), genres.get(2)));
    }
}