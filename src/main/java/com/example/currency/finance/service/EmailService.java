package com.example.currency.finance.service;

import com.example.currency.finance.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    // Injects the email defined in application.properties
    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendPaymentSuccessEmail(User user, Double amount, String description) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(user.getEmail());
            message.setSubject("Payment Successful - WealthWise");

            String text = "Dear " + user.getFullName() + ",\n\n" +
                    "Your payment was successfully processed.\n\n" +
                    "Transaction Details:\n" +
                    "--------------------\n" +
                    "Description: " + description + "\n" +
                    "Amount: $" + String.format("%.2f", amount) + "\n" +
                    "Status: Paid via Card\n\n" +
                    "Thank you for using WealthWise!";

            message.setText(text);
            mailSender.send(message);
            System.out.println("Email sent successfully to: " + user.getEmail());

        } catch (Exception e) {
            // Logs the error but doesn't crash the application
            System.err.println("Failed to send email: " + e.getMessage());
        }
    }
}