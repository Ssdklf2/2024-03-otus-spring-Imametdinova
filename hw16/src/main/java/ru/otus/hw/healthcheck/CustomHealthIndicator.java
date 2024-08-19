package ru.otus.hw.healthcheck;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class CustomHealthIndicator implements HealthIndicator {

    private final Random random = new Random();

    @Override
    public Health health() {
        int chance = random.nextInt(10);
        return chance > 8 ? Health.up().status(Status.UP).withDetail("Удача?", "Удача!").build() :
                Health.down().status(Status.UNKNOWN).withDetail("Удача?", "Неудача").build();
    }
}
