package com.demo.Logins.registration.userToken;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
@EnableMongoRepositories
@Configuration
public interface UserConformationRepository extends
        MongoRepository<UserConformationToken, String> {
    Optional<UserConformationToken> findByConfirmationToken(String confirmationToken);

}
