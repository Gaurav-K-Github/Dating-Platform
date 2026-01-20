package com.dating.service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Service for sending emails
 * NOTE: Configure SMTP settings before using in production
 */
public class EmailService {

    // TODO: Move these to configuration file or environment variables
    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final String SMTP_PORT = "587";
    private static final String SMTP_USERNAME = "your-email@gmail.com"; // Configure this
    private static final String SMTP_PASSWORD = "your-app-password"; // Configure this
    private static final String FROM_EMAIL = "noreply@datingplatform.com";
    private static final String FROM_NAME = "Dating Platform";

    /**
     * Sends a verification email to the user
     * @param toEmail Recipient email address
     * @param userName User's first name
     * @param verificationToken Verification token
     * @param baseUrl Application base URL
     * @return true if email sent successfully, false otherwise
     */
    public static boolean sendVerificationEmail(String toEmail, String userName, 
                                               String verificationToken, String baseUrl) {
        try {
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", SMTP_HOST);
            props.put("mail.smtp.port", SMTP_PORT);

            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(SMTP_USERNAME, SMTP_PASSWORD);
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_EMAIL, FROM_NAME));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Verify Your Dating Platform Account");

            String verificationLink = baseUrl + "/verify?token=" + verificationToken;
            
            String htmlContent = "<!DOCTYPE html>" +
                "<html><head><style>" +
                "body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }" +
                ".container { max-width: 600px; margin: 0 auto; padding: 20px; }" +
                ".header { background: linear-gradient(to right, #ee6c4d, #293241); color: white; padding: 30px; text-align: center; border-radius: 10px 10px 0 0; }" +
                ".content { background: #f9f9f9; padding: 30px; border-radius: 0 0 10px 10px; }" +
                ".button { display: inline-block; padding: 15px 30px; background-color: #ee6c4d; color: white; text-decoration: none; border-radius: 5px; margin: 20px 0; }" +
                ".footer { text-align: center; margin-top: 20px; font-size: 12px; color: #666; }" +
                "</style></head><body>" +
                "<div class='container'>" +
                "<div class='header'><h1>Welcome to Dating Platform!</h1></div>" +
                "<div class='content'>" +
                "<p>Hi " + userName + ",</p>" +
                "<p>Thank you for registering with Dating Platform. To complete your registration and verify your email address, please click the button below:</p>" +
                "<div style='text-align: center;'>" +
                "<a href='" + verificationLink + "' class='button'>Verify Email Address</a>" +
                "</div>" +
                "<p>Or copy and paste this link into your browser:</p>" +
                "<p><a href='" + verificationLink + "'>" + verificationLink + "</a></p>" +
                "<p>This link will expire in 24 hours.</p>" +
                "<p>If you didn't create an account, please ignore this email.</p>" +
                "<p>Best regards,<br>The Dating Platform Team</p>" +
                "</div>" +
                "<div class='footer'>" +
                "<p>&copy; 2026 Dating Platform. All rights reserved.</p>" +
                "</div></div></body></html>";

            message.setContent(htmlContent, "text/html; charset=utf-8");

            Transport.send(message);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Sends a password reset email
     * @param toEmail Recipient email address
     * @param userName User's first name
     * @param resetToken Reset token
     * @param baseUrl Application base URL
     * @return true if email sent successfully, false otherwise
     */
    public static boolean sendPasswordResetEmail(String toEmail, String userName, 
                                                String resetToken, String baseUrl) {
        // TODO: Implement password reset email
        return false;
    }
}
