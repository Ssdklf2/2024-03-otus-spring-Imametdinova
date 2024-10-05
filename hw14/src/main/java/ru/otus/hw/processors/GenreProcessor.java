package ru.otus.hw.processors;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import ru.otus.hw.cache.CacheManager;
import ru.otus.hw.models.Genre;
import ru.otus.hw.models.JpaGenre;

@RequiredArgsConstructor
public class GenreProcessor implements ItemProcessor<Genre, JpaGenre> {

    private final CacheManager cacheManager;

    @Override
    public JpaGenre process(Genre mongoGenre) throws Exception {
        JpaGenre jpaGenre = JpaGenre.builder()
                .name(mongoGenre.getName())
                .build();
        cacheManager.addGenreToMap(mongoGenre.getId(), jpaGenre);
        return jpaGenre;
    }
}
