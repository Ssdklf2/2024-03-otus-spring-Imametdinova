package ru.otus.hw.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "authors")
public class Author {

    @Id
    @EqualsAndHashCode.Include
    private String id;

    @Field(name = "full_name")
    private String fullName;

    public Author(String fullName) {
        this.fullName = fullName;
    }
}
