package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.models.JpaAuthor;

public interface JpaAuthorRepository extends JpaRepository<JpaAuthor, Long> {
}
