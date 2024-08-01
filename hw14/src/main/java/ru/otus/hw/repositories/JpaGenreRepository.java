package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.models.JpaGenre;

public interface JpaGenreRepository extends JpaRepository<JpaGenre, Long> {
}
