package ru.otus.hw.service;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class TestServiceImplTest {

    @InjectMocks
    private TestServiceImpl testService;

    @Mock
    private IOService ioService;

    @Mock
    private QuestionDao dao;

    @Captor
    ArgumentCaptor<String> lineCaptor;

    @Captor
    ArgumentCaptor<Object[]> argsCaptor;

    @Test
    void executeTest() {
        var answers = List.of(
                new Answer("As", false),
                new Answer("Ar", false),
                new Answer("Au", true));
        var question = new Question("What is the chemical symbol for Gold?", answers);
        var questions = List.of(question);

        when(dao.findAll()).thenReturn(questions);

        testService.executeTest();

        verify(ioService, times(6)).printFormattedLine(lineCaptor.capture(), argsCaptor.capture());

        assertEquals("Please answer the questions below%n", lineCaptor.getAllValues().get(0));
        assertEquals("Question: %s", lineCaptor.getAllValues().get(1));

        List<Object[]> allArgsValues = argsCaptor.getAllValues();
        Object[] questionLine = allArgsValues.get(1);

        assertEquals("What is the chemical symbol for Gold?", questionLine[0]);

        Object[] answerWithNum = allArgsValues.get(2);
        assertEquals(1, answerWithNum[0]);
        assertEquals("As", answerWithNum[1]);
    }
}