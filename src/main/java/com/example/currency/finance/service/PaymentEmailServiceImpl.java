package com.example.currency.finance.service;

import com.example.currency.finance.dto.ExpenseRequest;
import com.example.currency.finance.model.User;
import com.example.currency.finance.service.PaymentEmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

@Service
public class PaymentEmailServiceImpl implements PaymentEmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String senderEmail;

    @Override
    public void sendPaymentReceipt(User user, ExpenseRequest expense) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom(senderEmail);
            helper.setTo(user.getEmail()); // Assuming User model has getEmail()
            helper.setSubject("Payment Confirmation - " + expense.getDescription());

            String htmlContent = generateHtmlReceipt(user, expense);
            helper.setText(htmlContent, true);

            mailSender.send(mimeMessage);
            System.out.println("Email sent successfully to " + user.getEmail());

        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("Error sending email: " + e.getMessage());
        }
    }

    private String generateHtmlReceipt(User user, ExpenseRequest expense) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy");
        String dateStr = (expense.getExpenseDate() != null) ?
                expense.getExpenseDate().format(formatter) : "Today";

        return String.format("""
            <!DOCTYPE html>
            <html>
            <head>
                <style>
                    body { font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px; }
                    .container { max-width: 600px; margin: 0 auto; background: #fff; padding: 20px; border-radius: 8px; box-shadow: 0 0 10px rgba(0,0,0,0.1); }
                    .header { background-color: #198754; color: #fff; padding: 15px; text-align: center; border-radius: 8px 8px 0 0; }
                    .content { padding: 20px; }
                    .amount { font-size: 24px; color: #198754; font-weight: bold; }
                    .details-table { width: 100%%; border-collapse: collapse; margin-top: 15px; }
                    .details-table td { padding: 10px; border-bottom: 1px solid #ddd; }
                    .footer { text-align: center; font-size: 12px; color: #777; margin-top: 20px; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1>Payment Successful</h1>
                    </div>
                    <div class="content">
                        <p>Hi %s,</p>
                        <p>Your payment has been processed successfully.</p>
                        
                        <div style="text-align: center; margin: 20px 0;">
                            <span class="amount">$%.2f</span>
                        </div>

                        <table class="details-table">
                            <tr>
                                <td><strong>Description:</strong></td>
                                <td>%s</td>
                            </tr>
                            <tr>
                                <td><strong>Category:</strong></td>
                                <td>%s</td>
                            </tr>
                            <tr>
                                <td><strong>Date:</strong></td>
                                <td>%s</td>
                            </tr>
                            <tr>
                                <td><strong>Payment Method:</strong></td>
                                <td>Card (Stripe)</td>
                            </tr>
                        </table>
                    </div>
                    <div class="footer">
                        <p>Thank you for using our Finance Dashboard.</p>
                    </div>
                </div>
            </body>
            </html>
            """,
                user.getFullName(),
                expense.getAmount(),
                expense.getDescription(),
                expense.getCategory(),
                dateStr
        );
    }
}