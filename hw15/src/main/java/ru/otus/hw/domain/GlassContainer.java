package ru.otus.hw.domain;

import lombok.Getter;
import lombok.experimental.Accessors;

import java.sql.Timestamp;
import java.util.List;

public class GlassContainer extends AbstractContainer {
    @Getter
    @Accessors(fluent = true)
    private static final List<String> GARBAGE_LIST =
            List.of("Bottle", "Jar", "Perfume bottle", "Can", "Broken glass");

    public GlassContainer(Timestamp creationTimestamp) {
        super(creationTimestamp);
    }
}
