package com.jasser.backendbna.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String senderEmail;

    public boolean sendEmail(String recipientEmail, String password) {
        MimeMessagePreparator preparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, "utf-8");
            messageHelper.setFrom(senderEmail);
            messageHelper.setTo(recipientEmail);
            messageHelper.setSubject("Your New Password");

            String htmlContent = "<html>"
                    + "<head>"
                    + "<style>"
                    + "body { font-family: Arial, sans-serif; color: #333; }"
                    + ".container { width: 80%; margin: auto; padding: 20px; border: 1px solid #ddd; border-radius: 5px; background-color: #f9f9f9; }"
                    + ".header { background-color: #4CAF50; color: white; padding: 10px; text-align: center; border-radius: 5px 5px 0 0; }"
                    + ".content { padding: 20px; }"
                    + ".footer { text-align: center; margin-top: 20px; font-size: 0.8em; color: #888; }"
                    + "</style>"
                    + "</head>"
                    + "<body>"
                    + "<div class='container'>"
                    + "<div class='header'>"
                    + "<h1>Password Reset</h1>"
                    + "</div>"
                    + "<div class='content'>"
                    + "<p>Hello,</p>"
                    + "<p>Your new password is: <strong style='color: #4CAF50;'>" + password + "</strong></p>"
                    + "<p>If you did not request this password change, please ignore this email.</p>"
                    + "</div>"
                    + "<div class='footer'>"
                    + "<p>Thank you!</p>"
                    + "<p>&copy; 2024 Your Company</p>"
                    + "</div>"
                    + "</div>"
                    + "</body>"
                    + "</html>";

            messageHelper.setText(htmlContent, true); // true indicates HTML content
        };

        try {
            mailSender.send(preparator);
            logger.info("Email sent successfully to {}", recipientEmail);
            return true; // Indicate success
        } catch (Exception e) {
            logger.error("Failed to send email to {}: {}", recipientEmail, e.getMessage());
            return false; // Indicate failure
        }
    }
}
