package ru.otus.hw.service;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.hw.config.AppProperties;
import ru.otus.hw.dao.CsvQuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;


@DisplayName("Проверка сервиса тестирования студентов")
@SpringBootTest
class TestServiceImplTest {

    @Autowired
    private TestServiceImpl testService;

    @MockBean
    private LocalizedIOServiceImpl ioService;

    @MockBean
    private CsvQuestionDao questionDao;

    @MockBean
    private AppProperties config;

    @DisplayName("должен быть один правильный ответ")
    @Test
    void executeTestForAndGetOne() {
        var answers = List.of(
                new Answer("As", false),
                new Answer("Ar", false),
                new Answer("Au", true));
        var question = new Question("What is the chemical symbol for Gold?", answers);
        var questions = List.of(question);
        var student = new Student("name", "lastname");

        when(questionDao.findAll()).thenReturn(questions);
        when(config.getQuestionCount()).thenReturn(1);
        when(ioService.readIntForRangeWithPromptLocalized(anyInt(), anyInt(), anyString(), anyString())).thenReturn(3);

        var testResult = testService.executeTestFor(student);

        assertEquals(1, testResult.getRightAnswersCount());
    }
}