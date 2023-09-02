package com.demo.Logins.registration.userToken;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;
import lombok.AllArgsConstructor;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserConformationTokenService {

    @Autowired
    private final UserConformationRepository userConfRep;

    @Autowired
    private MongoClient mongoClient;
    public void saveConfirmationToken(UserConformationToken confirmationToken) {

        userConfRep.save(confirmationToken);
    }

    public Optional<UserConformationToken> getConfirmationToken(String confirmationToken) {
        return userConfRep.findByConfirmationToken(confirmationToken);
    }

   public void activateConfirmed(String token) {
       MongoCollection<Document> collection = mongoClient.getDatabase("Records").getCollection("confirmationToken");
       UpdateResult result = collection.updateOne(Filters.eq("confirmationToken", token), Updates.set("confirmTime", LocalDateTime.now()));
       if (result.getModifiedCount() == 0) {
           throw new IllegalStateException("User not found");
       }
   }

}
