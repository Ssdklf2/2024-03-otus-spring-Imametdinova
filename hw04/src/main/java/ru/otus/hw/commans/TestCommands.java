package ru.otus.hw.commans;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.service.TestRunnerService;

@RequiredArgsConstructor
@ShellComponent
public class TestCommands {

    private final TestRunnerService testRunnerService;

    @ShellMethod(key = "start", value = "Starts testing")
    public void start() {
        testRunnerService.run();
    }
}
