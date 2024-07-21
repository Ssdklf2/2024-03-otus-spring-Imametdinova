package ru.otus.hw.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.otus.hw.models.User;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {

    @Query("{ 'username': :#{#username} }")
    Optional<User> findByUsername(@Param("username") String username);
}
