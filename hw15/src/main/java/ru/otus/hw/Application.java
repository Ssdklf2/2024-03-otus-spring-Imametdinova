package ru.otus.hw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import ru.otus.hw.services.GarbageSorter;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(Application.class, args);
        GarbageSorter garbageSorter = ctx.getBean(GarbageSorter.class);
        garbageSorter.startSortGarbageLoop();
    }
}