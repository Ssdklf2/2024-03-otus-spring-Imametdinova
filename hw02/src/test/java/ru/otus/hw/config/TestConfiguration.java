package ru.otus.hw.config;

import org.springframework.context.annotation.Bean;
import ru.otus.hw.dao.CsvQuestionDao;

public class TestConfiguration {

    @Bean
    public TestFileNameProvider testFileNameProvider() {
        return new AppProperties();
    }

    @Bean
    public CsvQuestionDao csvQuestionDao(TestFileNameProvider provider) {
        return new CsvQuestionDao(provider);
    }
}
