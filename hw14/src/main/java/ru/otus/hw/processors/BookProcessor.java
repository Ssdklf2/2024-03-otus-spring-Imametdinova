package ru.otus.hw.processors;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import ru.otus.hw.cache.CacheManager;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.JpaAuthor;
import ru.otus.hw.models.JpaBook;
import ru.otus.hw.models.JpaGenre;

@RequiredArgsConstructor
public class BookProcessor implements ItemProcessor<Book, JpaBook> {

    private final CacheManager cacheManager;

    @Override
    public JpaBook process(Book mongoBook) throws Exception {
        JpaAuthor jpaAuthor = cacheManager.getJpaAuthorByMongoAuthorId(mongoBook.getAuthor().getId());
        JpaGenre jpaGenre = cacheManager.getJpaGenreByMongoGenreId(mongoBook.getGenre().getId());
        return JpaBook.builder()
                .title(mongoBook.getTitle())
                .jpaAuthor(jpaAuthor)
                .jpaGenre(jpaGenre)
                .build();
    }
}
