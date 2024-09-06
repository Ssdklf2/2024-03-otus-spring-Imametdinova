package ru.otus.hw.domain;

import lombok.Getter;
import lombok.experimental.Accessors;

import java.sql.Timestamp;
import java.util.List;

public class PaperContainer extends AbstractContainer {
    @Getter
    @Accessors(fluent = true)
    private static final List<String> GARBAGE_LIST =
            List.of("Newspaper", "Cardboard", "Office paper", "Brochure", "Paper bag");

    public PaperContainer(Timestamp creationTimestamp) {
        super(creationTimestamp);
    }
}
