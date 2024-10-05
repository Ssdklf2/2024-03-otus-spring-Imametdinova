package ru.otus.hw.processors;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import ru.otus.hw.cache.CacheManager;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.JpaAuthor;

@RequiredArgsConstructor
public class AuthorProcessor implements ItemProcessor<Author, JpaAuthor> {

    private final CacheManager cacheManager;

    @Override
    public JpaAuthor process(Author mongoAuthor) throws Exception {
        JpaAuthor jpaAuthor = JpaAuthor.builder()
                .fullName(mongoAuthor.getFullName())
                .build();
        cacheManager.addAuthorToMap(mongoAuthor.getId(), jpaAuthor);
        return jpaAuthor;
    }
}
