package ru.otus.hw.dao;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.hw.config.AppProperties;
import ru.otus.hw.config.TestConfiguration;
import ru.otus.hw.domain.Question;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(initializers = {ConfigDataApplicationContextInitializer.class}, classes = {TestConfiguration.class})
@EnableConfigurationProperties(AppProperties.class)
@ActiveProfiles("test")
class CsvQuestionDaoTest {

    @Autowired
    private CsvQuestionDao csvQuestionDao;

    @DisplayName("должен быть список из 4 вопросов")
    @Test
    void findAllTest() {
        List<Question> questions = csvQuestionDao.findAll();
        assertEquals(4, questions.size());
    }
}