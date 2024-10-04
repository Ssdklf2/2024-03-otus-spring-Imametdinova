package ru.otus.hw.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.MongoPagingItemReader;
import org.springframework.batch.item.data.builder.MongoPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.transaction.PlatformTransactionManager;
import ru.otus.hw.cache.CacheManager;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.models.JpaAuthor;
import ru.otus.hw.models.JpaBook;
import ru.otus.hw.models.JpaGenre;
import ru.otus.hw.processors.AuthorProcessor;
import ru.otus.hw.processors.BookProcessor;
import ru.otus.hw.processors.GenreProcessor;
import ru.otus.hw.repositories.JpaAuthorRepository;
import ru.otus.hw.repositories.JpaBookRepository;
import ru.otus.hw.repositories.JpaGenreRepository;

import java.util.Collections;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
@Log4j2
public class JobConfig {

    public static final String MIGRATION_JOB = "migrationJob";

    private static final int CHUNK_SIZE = 2;

    private static final int PAGE_SIZE = 100;

    private final JobRepository jobRepository;

    private final PlatformTransactionManager platformTransactionManager;

    private final JpaAuthorRepository jpaAuthorRepository;

    private final JpaBookRepository jpaBookRepository;

    private final JpaGenreRepository jpaGenreRepository;

    private final Map<String, Sort.Direction> sortsMap = Collections.singletonMap("id", Sort.Direction.ASC);

    @Bean
    public MongoPagingItemReader<Author> mongoAuthorReader(MongoOperations operations) {
        return new MongoPagingItemReaderBuilder<Author>()
                .name("mongoAuthorReader")
                .jsonQuery("{}")
                .template(operations)
                .targetType(Author.class)
                .pageSize(PAGE_SIZE)
                .sorts(sortsMap)
                .build();
    }

    @Bean
    public MongoPagingItemReader<Genre> mongoGenreReader(MongoOperations operations) {
        return new MongoPagingItemReaderBuilder<Genre>()
                .name("mongoGenreReader")
                .jsonQuery("{}")
                .template(operations)
                .targetType(Genre.class)
                .pageSize(PAGE_SIZE)
                .sorts(sortsMap)
                .build();
    }

    @Bean
    public MongoPagingItemReader<Book> mongoBookReader(MongoOperations operations) {
        return new MongoPagingItemReaderBuilder<Book>()
                .name("mongoBookReader")
                .jsonQuery("{}")
                .template(operations)
                .targetType(Book.class)
                .pageSize(PAGE_SIZE)
                .sorts(sortsMap)
                .build();
    }

    @Bean
    public AuthorProcessor authorProcessor() {
        return new AuthorProcessor(cacheManager());
    }

    @Bean
    public GenreProcessor genreProcessor() {
        return new GenreProcessor(cacheManager());
    }

    @Bean
    public BookProcessor bookProcessor() {
        return new BookProcessor(cacheManager());
    }

    @Bean
    public CacheManager cacheManager() {
        return new CacheManager();
    }

    @Bean
    public ItemWriter<JpaAuthor> jpaAuthorWriter() {
        return jpaAuthorRepository::saveAll;
    }

    @Bean
    public ItemWriter<JpaGenre> jpaGenreWriter() {
        return jpaGenreRepository::saveAll;
    }

    @Bean
    public ItemWriter<JpaBook> jpaBookWriter() {
        return jpaBookRepository::saveAll;
    }

    @Bean
    public Step migrateAuthorsStep(ItemReader<Author> reader,
                                   ItemWriter<JpaAuthor> writer,
                                   AuthorProcessor authorProcessor) {
        return new StepBuilder("migrateAuthorsStep", jobRepository)
                .<Author, JpaAuthor>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(reader)
                .processor(authorProcessor)
                .writer(writer)
                .build();
    }

    @Bean
    public Step migrateGenresStep(ItemReader<Genre> reader,
                                  ItemWriter<JpaGenre> writer,
                                  GenreProcessor genreProcessor) {
        return new StepBuilder("migrateGenresStep", jobRepository)
                .<Genre, JpaGenre>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(reader)
                .processor(genreProcessor)
                .writer(writer)
                .build();
    }

    @Bean
    public Step migrateBooksStep(ItemReader<Book> reader,
                                 ItemWriter<JpaBook> writer,
                                 BookProcessor bookProcessor) {
        return new StepBuilder("migrateBooksStep", jobRepository)
                .<Book, JpaBook>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(reader)
                .processor(bookProcessor)
                .writer(writer)
                .build();
    }

    @Bean
    public Job migrationJob(Step migrateAuthorsStep, Step migrateGenresStep, Step migrateBooksStep) {
        return new JobBuilder(MIGRATION_JOB, jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(migrateAuthorsStep)
                .next(migrateGenresStep)
                .next(migrateBooksStep)
                .build();
    }
}
