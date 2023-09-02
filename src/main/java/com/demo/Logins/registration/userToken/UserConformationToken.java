package com.demo.Logins.registration.userToken;

import com.demo.Logins.appUser.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "confirmationToken")
public class UserConformationToken {

    @Id
    private String id;
    private String confirmationToken;
    private LocalDateTime createdTime;
    private LocalDateTime expireTime;
    private LocalDateTime confirmTime;

    private String userID;

    public UserConformationToken(String confirmationToken, LocalDateTime createdTime,
                                 LocalDateTime expireTime, String userId) {
        this.confirmationToken = confirmationToken;
        this.createdTime = createdTime;
        this.expireTime = expireTime;
        this.userID = userId;
    }
}
