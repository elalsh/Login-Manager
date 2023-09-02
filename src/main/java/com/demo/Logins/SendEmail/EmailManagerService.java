package com.demo.Logins.SendEmail;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@AllArgsConstructor
public class EmailManagerService implements EmailManager{

    private final static Logger logger = LoggerFactory.getLogger(EmailManagerService.class);

    private final JavaMailSender sendMail;


    @Override
    @Async
    public void send(String recipient, String emailBody) {
        try {
            MimeMessage mimeMessage = sendMail.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(emailBody, true);
            helper.setTo(recipient);
            helper.setSubject("Confirm your email");
            helper.setFrom("hello@amigoscode.com");
            sendMail.send(mimeMessage);
        } catch (MessagingException e) {
            logger.error("Email did not send: Error occurred", e);
            throw new IllegalStateException("Email did not send");
        }
    }
}
