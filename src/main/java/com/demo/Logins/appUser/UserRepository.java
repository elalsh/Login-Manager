package com.demo.Logins.appUser;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@EnableMongoRepositories
@Configuration
public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findUserByEmail(String email);

    Optional<User> findById(String id);

    @Query("{'email' : ?0}")
    void enableUser(String email);
}
