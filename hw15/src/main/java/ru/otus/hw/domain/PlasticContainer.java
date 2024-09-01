package ru.otus.hw.domain;

import lombok.Getter;
import lombok.experimental.Accessors;

import java.sql.Timestamp;
import java.util.List;

public class PlasticContainer extends AbstractContainer {
    @Getter
    @Accessors(fluent = true)
    private static final List<String> GARBAGE_LIST =
            List.of("Plastic bottle", "Bag", "Lid", "Yogurt cup", "Packaging");

    public PlasticContainer(Timestamp creationTimestamp) {
        super(creationTimestamp);
    }
}
