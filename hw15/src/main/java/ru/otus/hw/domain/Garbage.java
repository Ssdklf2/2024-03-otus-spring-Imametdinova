package ru.otus.hw.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
public final class Garbage {

    private final String name;

    @Setter
    private Type type;

    public Garbage(String name) {
        this.name = name;
    }
}