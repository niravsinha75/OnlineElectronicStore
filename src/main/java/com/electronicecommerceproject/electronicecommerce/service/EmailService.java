package com.electronicecommerceproject.electronicecommerce.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendResetPasswordEmail(String to, String resetLink) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Password Reset Request");
        message.setText("Click the link below to reset your password:\n" + resetLink + 
                        "\n\nThis link will expire in 15 minutes.");

        mailSender.send(message);
    }
    
    public void sendOrderStatusEmail(String to, String status, String customer, String productName, int orderId) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject("Online Electronics Store Order Status");

            // HTML email content
            String htmlContent = "<p>Dear <b>" + customer + "</b>,</p>" +
                    "<p>Your order for <b>" + productName + "</b> with Order ID <b>" + orderId + "</b> has been <b>" + status + "</b>.</p>" +
                    "<p>Kindly click the link below to login:</p>" +
                    "<p><a href='http://localhost:8080/login'>Login to Your Account</a></p>" +
                    "<p>Best Regards,<br><b>Online Electronics Store</b></p>";

            helper.setText(htmlContent, true); // Enable HTML format

            mailSender.send(message);
            System.out.println("Order status email sent successfully!");
        } catch (MessagingException e) {
            System.err.println("Error sending email: " + e.getMessage());
        }
    }
}
