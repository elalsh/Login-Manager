package com.demo.Logins.appUser;

import com.demo.Logins.registration.userToken.UserConformationToken;
import com.demo.Logins.registration.userToken.UserConformationTokenService;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;
import lombok.AllArgsConstructor;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;


@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {
    private final String userWarning = "User with email %s not found";

    //@Autowired
    private final UserRepository userRepository;


    @Autowired
    private MongoClient mongoClient;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final UserConformationTokenService ucts;
    @Override
    public UserDetails loadUserByUsername(String userEmail)
            throws UsernameNotFoundException {

        return userRepository.findUserByEmail(userEmail).orElseThrow(() ->
                new UsernameNotFoundException(String.format(userWarning, userEmail)));
    }

    public String registerUser(User user)  {
        // check if the user exists
        boolean userPresent = userRepository.findUserByEmail(user.getEmail()).isPresent();
        boolean resend = false;
        if (userPresent) {
            // If the user exists it could also be possible that it didn't conform
            User validateUser = userRepository.findUserByEmail(user.getEmail()).orElse(null);
            if (!validateUser.isEnabled() & validateUser.getFirstName().equals(user.getFirstName())
                & validateUser.getLastName().equals(user.getLastName())) {
                    //throw new IllegalArgumentException("User exists but not enabled");
                    resend = true;
            } else {
                throw new IllegalStateException("Email is already taken");
            }

        }
        if (!resend) {
            String passEncoded = bCryptPasswordEncoder.encode(user.getPassword());
            user.setPassword(passEncoded);
            userRepository.save(user);
        }

        String uuidToken = UUID.randomUUID().toString();
        User current_User = userRepository.findUserByEmail(user.getEmail()).orElse(null);
        UserConformationToken tokenUser = new UserConformationToken(
            uuidToken, LocalDateTime.now(), LocalDateTime.now().plusMinutes(15), current_User.getId());

        ucts.saveConfirmationToken(tokenUser);

        // Send Email
        return uuidToken;
    }

    public void enableUser(String email) {
        MongoCollection<Document> collection = mongoClient.getDatabase("Records").getCollection("registration");
        UpdateResult result = collection.updateOne(Filters.eq("email", email), Updates.set("enabled", true));
        if (result.getModifiedCount() == 0) {
            throw new IllegalStateException("User not found");
        }
    }

    public String loginUser(String email, String password) {
        boolean present = userRepository.findUserByEmail(email).isPresent();
        if (!present) {
            throw new IllegalArgumentException("User Doesn't Exist");
        }
        User user = userRepository.findUserByEmail(email).orElse(null);
        if (user.isEnabled() && user != null &&
                bCryptPasswordEncoder.matches(password, user.getPassword())) {
            return "Success";
        }
        return "Failed";
    }
}
