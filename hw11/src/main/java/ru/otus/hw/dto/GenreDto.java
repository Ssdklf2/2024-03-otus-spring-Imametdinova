package ru.otus.hw.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.hw.models.Genre;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenreDto {

    private String id;

    private String name;

    public Genre toDomainObject() {
        return new Genre(id, name);
    }

    public static GenreDto fromDomainObject(Genre genre) {
        return new GenreDto(genre.getId(), genre.getName());
    }
}