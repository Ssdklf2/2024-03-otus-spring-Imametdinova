package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.repositories.AuthorRepository;

@RestController
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorRepository authorRepository;

    @GetMapping(value = "/api/authors", produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<AuthorDto> getBooks() {
        return authorRepository.findAll().map(AuthorDto::fromDomainObject);
    }
}
