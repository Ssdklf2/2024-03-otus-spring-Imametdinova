package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.repositories.GenreRepository;


@RestController
@RequiredArgsConstructor
public class GenreController {

    private final GenreRepository genreRepository;

    @GetMapping(value = "/api/genres", produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<GenreDto> getBooks() {
        return genreRepository.findAll().map(GenreDto::fromDomainObject);
    }
}
