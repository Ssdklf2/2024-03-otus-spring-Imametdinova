package ru.otus.hw.services;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookRequestDto;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Genre;

import java.util.List;

@Service
@Log4j2
public class SimulationAnotherBookBookSourceService implements AnotherBookSourceService {

    @Override
    public BookDto findById(String id) {
        log.info("Searching for book with id {} in alternative source...", id);
        return new BookDto("SomeId", "SomeTitle", new Author(), new Genre());
    }

    @Override
    public List<BookDto> findAll() {
        log.info("Searching for books in alternative source...");
        return List.of(new BookDto("SomeId", "SomeTitle", new Author(), new Genre()));
    }

    @Override
    public BookDto insert(BookRequestDto bookDto) {
        log.info("Inserting a book with id {} in alternative source...", bookDto.getId());
        return new BookDto("SomeId", "SomeTitle", new Author(), new Genre());
    }

    @Override
    public BookDto update(BookRequestDto bookDto) {
        log.info("Updating a book with id {} in alternative source...", bookDto.getId());
        return new BookDto("SomeId", "SomeTitle", new Author(), new Genre());
    }

    @Override
    public void deleteById(String id) {
        log.info("Deleting book with id {} in alternative source...", id);
    }
}
