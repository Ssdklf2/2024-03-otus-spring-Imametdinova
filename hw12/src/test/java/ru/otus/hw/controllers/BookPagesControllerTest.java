package ru.otus.hw.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.security.CustomUserDetailsService;
import ru.otus.hw.security.SecurityConfiguration;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CommentService;
import ru.otus.hw.services.GenreService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookPagesController.class)
@Import({SecurityConfiguration.class})
class BookPagesControllerTest {

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

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    private static final String DEFAULT_ID = "1";

    @Test
    void listPage_andGetUnauthorizedException() throws Exception {
        mvc.perform(get("/books"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testListPage_andGetUnauthorizedException() throws Exception {
        mvc.perform(get("/books/" + DEFAULT_ID))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void createPage_andGetUnauthorizedException() throws Exception {
        mvc.perform(get("/books/create"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void editPage_andGetUnauthorizedException() throws Exception {
        mvc.perform(get("/books/edit/" + DEFAULT_ID))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void loginIsAvailable() throws Exception {
        mvc.perform(get("/login"))
                .andExpect(status().isOk());
    }
}