package ru.otus.hw.services;

import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookRequestDto;

import java.util.List;

public interface AnotherBookSourceService {

    BookDto findById(String id);

    List<BookDto> findAll();

    BookDto insert(BookRequestDto bookDto);

    BookDto update(BookRequestDto bookDto);

    void deleteById(String id);
}
