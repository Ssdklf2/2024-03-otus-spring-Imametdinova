package ru.otus.hw.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.config.TestConfig;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    private final TestConfig testConfig;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");
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
        ioService.printFormattedLine("Question: %s", question.text());
        ioService.printLine("Answers: ");
        var answers = question.answers();
        int i;
        int numOfCorrectAnswer = 0;
        for (i = 0; i < answers.size(); i++) {
            var answer = answers.get(i);
            ioService.printFormattedLine("%d. %s", i + 1, answer.text());
            if (answer.isCorrect()) {
                numOfCorrectAnswer = i + 1;
            }
        }
        int numOfAnswer = ioService.readIntForRangeWithPrompt(1, i, "Enter the number of the answer",
                "Please enter a valid number");
        return numOfAnswer == numOfCorrectAnswer;
    }
}
