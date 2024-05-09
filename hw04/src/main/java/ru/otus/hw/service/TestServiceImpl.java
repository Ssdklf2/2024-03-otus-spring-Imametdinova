package ru.otus.hw.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.config.TestConfig;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final LocalizedIOService ioService;

    private final QuestionDao questionDao;

    private final TestConfig testConfig;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printLineLocalized("TestService.answer.the.questions");
        ioService.printLine("");

        var questions = questionDao.findAll();
        var testResult = new TestResult(student);
        fillTestResult(questions, testResult);
        return testResult;
    }

    private void fillTestResult(List<Question> questions, TestResult testResult) {
        questions.stream()
                .limit(testConfig.getQuestionCount())
                .forEach(question -> {
                    var isAnswerValid = askQuestion(question);
                    testResult.applyAnswer(question, isAnswerValid);
                });
    }

    private boolean askQuestion(Question question) {
        ioService.printFormattedLineLocalized("TestService.question", question.text());
        ioService.printLineLocalized("TestService.answers");
        var answers = question.answers();
        int i;
        int numOfCorrectAnswer = 0;
        for (i = 0; i < answers.size(); i++) {
            numOfCorrectAnswer = getNumOfCorrectAnswer(answers.get(i), i + 1, numOfCorrectAnswer);
        }
        int numOfAnswer = ioService.readIntForRangeWithPromptLocalized(1, i, "TestService.number.of.answer",
                "TestService.enter.valid.answer");
        return numOfAnswer == numOfCorrectAnswer;
    }

    private int getNumOfCorrectAnswer(Answer answer, int num, int numOfCorrectAnswer) {
        ioService.printFormattedLine("%d. %s", num, answer.text());
        if (answer.isCorrect()) {
            numOfCorrectAnswer = num;
        }
        return numOfCorrectAnswer;
    }
}
