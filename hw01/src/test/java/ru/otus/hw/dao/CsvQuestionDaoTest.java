package ru.otus.hw.dao;

import java.util.List;
import org.junit.jupiter.api.Test;
import ru.otus.hw.config.AppProperties;
import ru.otus.hw.domain.Question;

import static org.junit.jupiter.api.Assertions.*;

class CsvQuestionDaoTest {

    private CsvQuestionDao csvQuestionDao;

    @Test
    void findAllTest() {
        csvQuestionDao = new CsvQuestionDao(new AppProperties("testQuestions_3.csv"));
        List<Question> questions = csvQuestionDao.findAll();
        assertEquals(3, questions.size());
    }

    @Test
    void findAllAndGetOneTest() {
        csvQuestionDao = new CsvQuestionDao(new AppProperties("testQuestions_1.csv"));
        List<Question> questions = csvQuestionDao.findAll();
        assertEquals(1, questions.size());
    }

    @Test
    void findAllAndGetEmptyListTest() {
        csvQuestionDao = new CsvQuestionDao(new AppProperties("testQuestions_empty.csv"));
        List<Question> questions = csvQuestionDao.findAll();
        assertEquals(0, questions.size());
    }
}