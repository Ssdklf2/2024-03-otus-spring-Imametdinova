package ru.otus.hw.dao;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

@Repository
@RequiredArgsConstructor
public class CsvQuestionDao implements QuestionDao {

    private final TestFileNameProvider fileNameProvider;

    @Override
    public List<Question> findAll() {
        String fileName = fileNameProvider.getTestFileName();
        ClassLoader classLoader = getClass().getClassLoader();
        try (InputStream inputStream = classLoader.getResourceAsStream(fileName)) {
            if (inputStream == null) {
                throw new QuestionReadException("File not find: " + fileName, new IllegalArgumentException());
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            CsvToBeanBuilder<QuestionDto> csvToBeanBuilder = new CsvToBeanBuilder<>(reader);
            CsvToBean<QuestionDto> questionDtos = csvToBeanBuilder.withSkipLines(1)
                    .withType(QuestionDto.class)
                    .withSeparator(';').build();
            return questionDtos.stream().map(QuestionDto::toDomainObject).collect(Collectors.toList());
        } catch (IOException e) {
            throw new QuestionReadException("Error while processing file " + fileName, e);
        }
    }
}
