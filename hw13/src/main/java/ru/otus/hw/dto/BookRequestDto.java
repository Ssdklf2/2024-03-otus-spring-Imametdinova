package ru.otus.hw.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BookRequestDto {

    private String id;

    @NotBlank(message = "{title-should-not-be-blank}")
    private String title;

    private String authorId;

    private String genreId;
}
