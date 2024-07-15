package ru.otus.hw.controllers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

@Controller
@AllArgsConstructor
public class BookPagesController {

    private final BookRepository bookRepository;

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    @GetMapping("/books")
    public String listPage(Model model) {
        return "books/list";
    }

    @GetMapping("/books/{id}")
    public String listPage(@PathVariable String id, Model model) {
        model.addAttribute("book", bookRepository.findById(id).map(BookDto::fromDomainObject).block());
        return "books/book";
    }

    @GetMapping("/books/create")
    public String createPage(Model model) {
        model.addAttribute("book", new BookDto());
        model.addAttribute("authors", authorRepository.findAll().collectList().block());
        model.addAttribute("genres", genreRepository.findAll().collectList().block());
        return "books/create";
    }


    @GetMapping("/books/edit/{id}")
    public String editPage(@PathVariable String id, Model model) {
        BookDto book = bookRepository.findById(id).map(BookDto::fromDomainObject).block();
        model.addAttribute("book", book);
        model.addAttribute("authors", authorRepository.findAll().collectList().block());
        model.addAttribute("genres", genreRepository.findAll().collectList().block());
        return "books/edit";
    }
}