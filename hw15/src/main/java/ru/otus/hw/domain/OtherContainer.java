package ru.otus.hw.domain;

import lombok.Getter;
import lombok.experimental.Accessors;

import java.sql.Timestamp;
import java.util.List;

public class OtherContainer extends AbstractContainer {
    @Getter
    @Accessors(fluent = true)
    private static final List<String> GARBAGE_LIST =
            List.of("Food scrap", "Light bulb", "Battery");

    public OtherContainer(Timestamp creationTimestamp) {
        super(creationTimestamp);
    }
}
