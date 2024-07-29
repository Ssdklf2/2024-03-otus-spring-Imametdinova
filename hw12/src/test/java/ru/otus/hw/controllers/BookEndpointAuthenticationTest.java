package ru.otus.hw.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookRequestDto;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Genre;
import ru.otus.hw.security.CustomUserDetailsService;
import ru.otus.hw.security.SecurityConfiguration;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CommentService;
import ru.otus.hw.services.GenreService;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {BookPagesController.class, BookController.class})
@Import({SecurityConfiguration.class})
class BookEndpointAuthenticationTest {

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

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    private static final String DEFAULT_ID = "1";

    @ParameterizedTest(name = "{0} {1} for user {2} should return {4} status")
    @MethodSource("getTestData")
    void shouldReturnExpectedStatus(String method, String url, String username, String[] roles, int expectedStatus,
                                    BookRequestDto bookRequest) throws Exception {
        var requestBuilder = methodRequestBuilder(method, url);

        if (username != null) {
            requestBuilder.with(user(username).roles(roles));
        }
        if (bookRequest != null) {
            requestBuilder.content(objectMapper.writeValueAsString(bookRequest))
                    .contentType(MediaType.APPLICATION_JSON);
        }
        when(bookService.findById(DEFAULT_ID)).thenReturn(getDefaultBookDto());

        mvc.perform(requestBuilder)
                .andExpect(status().is(expectedStatus));
    }

    private MockHttpServletRequestBuilder methodRequestBuilder(String method, String url) {
        Map<String, Function<String, MockHttpServletRequestBuilder>> methodMap = Map.of(
                "get", MockMvcRequestBuilders::get,
                "post", MockMvcRequestBuilders::post,
                "put", MockMvcRequestBuilders::put,
                "delete", MockMvcRequestBuilders::delete);
        return methodMap.get(method).apply(url);
    }

    public static Stream<Arguments> getTestData() {
        var roles = new String[]{"USER"};
        var bookRequest = new BookRequestDto(null, "title", "a1", "g1");
        return Stream.of(
                Arguments.of("get", "/books", "user", roles, 200, null),
                Arguments.of("get", "/books/" + DEFAULT_ID, "user", roles, 200, null),
                Arguments.of("get", "/books/create", "user", roles, 200, null),
                Arguments.of("get", "/books/edit/" + DEFAULT_ID, "user", roles, 200, null),
                Arguments.of("get", "/api/books", "user", roles, 200, null),
                Arguments.of("get", "/api/books/" + DEFAULT_ID, "user", roles, 200, null),
                Arguments.of("post", "/api/books", "user", roles, 200, bookRequest),
                Arguments.of("put", "/api/books/" + DEFAULT_ID, "user", roles, 200, bookRequest),
                Arguments.of("delete", "/api/books/" + DEFAULT_ID, "user", roles, 204, null),
                Arguments.of("get", "/books", null, null, 401, null),
                Arguments.of("get", "/books", null, null, 401, null),
                Arguments.of("get", "/books/", null, null, 401, null),
                Arguments.of("get", "/books/create", null, null, 401, null),
                Arguments.of("get", "/books/edit/", null, null, 401, null),
                Arguments.of("get", "/api/books", null, null, 401, null),
                Arguments.of("get", "/api/books/", null, null, 401, null),
                Arguments.of("post", "/api/books", null, null, 401, null),
                Arguments.of("put", "/api/books/", null, null, 401, null),
                Arguments.of("delete", "/api/books/", null, null, 401, null),
                Arguments.of("get", "/login", null, null, 200, null)
        );
    }

    private BookDto getDefaultBookDto() {
        return new BookDto(DEFAULT_ID, "title", new Author(), new Genre());
    }
}