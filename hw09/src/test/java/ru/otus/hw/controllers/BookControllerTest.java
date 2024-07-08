package ru.otus.hw.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Genre;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CommentService;
import ru.otus.hw.services.GenreService;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mvc;

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
        mvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.view().name("books/list"));
    }

    @Test
    void getBook() throws Exception {
        String id = "1";
        given(bookService.findById(id)).willReturn(books.get(0));
        mvc.perform(get("/books/" + id))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.view().name("books/book"));
    }

    @Test
    void editPage() throws Exception {
        String id = "1";
        given(bookService.findById(id)).willReturn(books.get(0));
        mvc.perform(get("/books/edit?id=" + id))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.view().name("books/edit"));
    }

    @Test
    void editBook() throws Exception {
        String id = "1";
        mvc.perform(post("/books/edit?id=" + id)
                        .param("id", "1")
                        .param("title", "title1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/books"));
    }

    @Test
    void removeBook() throws Exception {
        String id = "1";
        mvc.perform(post("/books/remove?id=" + id))
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/books"));
    }

    @Test
    void createPage() throws Exception {
        mvc.perform(get("/books/create"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.view().name("books/create"));
    }

    @Test
    void createBook() throws Exception {
        String id = "1";
        mvc.perform(post("/books/create")
                        .param("id", id)
                        .param("title", "title2"))
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/books"));
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