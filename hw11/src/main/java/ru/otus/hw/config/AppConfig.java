package ru.otus.hw.config;


import com.github.cloudyrock.spring.v5.EnableMongock;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@EnableReactiveMongoRepositories
@EnableMongock
public class AppConfig {

}
