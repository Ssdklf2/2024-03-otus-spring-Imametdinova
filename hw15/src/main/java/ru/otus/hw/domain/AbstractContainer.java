package ru.otus.hw.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public abstract class AbstractContainer {

    private final Collection<Garbage> sortedGarbage = new ArrayList<>();

    @Getter
    @EqualsAndHashCode.Include
    private final Timestamp creationTimestamp;

    public AbstractContainer(Timestamp creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    public void addAll(List<Garbage> garbageList) {
        sortedGarbage.addAll(garbageList);
    }

    public String toString() {
        return this.getClass().getSimpleName() + " contains " +
                sortedGarbage.stream().map(Garbage::getName).collect(Collectors.joining(", "));
    }
}
