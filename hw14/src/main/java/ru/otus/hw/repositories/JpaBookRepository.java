package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.models.JpaBook;

import java.util.List;
import java.util.Optional;

public interface JpaBookRepository extends JpaRepository<JpaBook, Long> {

    @EntityGraph(attributePaths = {"author", "genre"})
    Optional<JpaBook> findById(long id);

    @Override
    @EntityGraph(attributePaths = {"author", "genre"})
    List<JpaBook> findAll();
}
