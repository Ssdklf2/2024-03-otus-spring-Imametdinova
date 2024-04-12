package ru.otus.hw.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao dao;

    @Override
    public void executeTest() {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");
        List<Question> questions = dao.findAll();
        questions.forEach(question -> {
            printQuestion(question);
            printAnswers(question.answers());
            ioService.printFormattedLine("%n");
        });
    }

    private void printQuestion(Question question) {
        ioService.printFormattedLine("Question: %s", question.text());
    }

    private void printAnswers(List<Answer> answers) {
        ioService.printLine("Answers: ");
        for (int i = 0; i < answers.size(); i++) {
            ioService.printFormattedLine("%d. %s", i + 1, answers.get(i).text());
        }
    }
}
