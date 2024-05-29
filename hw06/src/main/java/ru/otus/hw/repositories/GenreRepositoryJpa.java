package ru.otus.hw.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Optional;

@Repository
public class GenreRepositoryJpa implements GenreRepository {


    @PersistenceContext
    private final EntityManager em;

    public GenreRepositoryJpa(EntityManager em) {
        this.em = em;
    }

    @Override
    public List<Genre> findAll() {
        var query = em.createQuery("select g from Genre g", Genre.class);
        return query.getResultList();
    }

    @Override
    public Optional<Genre> findById(long id) {
        return Optional.ofNullable(em.find(Genre.class, id));
    }
}
