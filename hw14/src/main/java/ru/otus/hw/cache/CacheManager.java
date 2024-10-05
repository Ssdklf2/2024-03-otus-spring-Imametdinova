package ru.otus.hw.cache;

import ru.otus.hw.models.JpaAuthor;
import ru.otus.hw.models.JpaGenre;

import java.util.HashMap;
import java.util.Map;

public class CacheManager {

    private final Map<String, JpaGenre> genreMap = new HashMap<>();

    private final Map<String, JpaAuthor> authorMap = new HashMap<>();

    public void addGenreToMap(String mongoGenreId, JpaGenre jpaGenre) {
        genreMap.put(mongoGenreId, jpaGenre);
    }

    public JpaGenre getJpaGenreByMongoGenreId(String mongoGenreId) {
        return genreMap.get(mongoGenreId);
    }

    public JpaAuthor getJpaAuthorByMongoAuthorId(String mongoAuthorId) {
        return authorMap.get(mongoAuthorId);
    }

    public void addAuthorToMap(String mongoAuthorId, JpaAuthor jpaAuthor) {
        authorMap.put(mongoAuthorId, jpaAuthor);
    }
}
