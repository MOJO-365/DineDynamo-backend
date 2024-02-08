package com.dinedynamo.services;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendNewPasswordEmail(String recipientEmail, String newPassword, String restaurantName) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(recipientEmail);
        message.setSubject("Password Reset for " + restaurantName);
        String emailContent = "Hello " + restaurantName + ",\n\n" +
                "We have received a request to reset your password for your " + restaurantName + " account.\n" +
                "Your new password is: " + newPassword + "\n\n" +
                "Please ensure to keep this password confidential and do not share it with anyone.\n" +
                "If you have any questions or concerns, please feel free to contact us.\n\n" +
                "Best regards,\n" +
                "The DineDynamo Team";
        message.setText(emailContent);
        mailSender.send(message);
    }
}

