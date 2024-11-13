package com.libraryman_api.email;

import com.libraryman_api.notification.NotificationRepository;
import com.libraryman_api.notification.NotificationStatus;
import com.libraryman_api.notification.Notifications;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Unified service class for sending emails asynchronously.
 * Handles both general email sending and notifications.
 */
@Service
public class EmailService implements EmailSender {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailService.class);

    private final NotificationRepository notificationRepository;
    private final JavaMailSender mailSender;

    @Value("${spring.mail.properties.domain_name}") // Domain name from application properties
    private String domainName;

    public EmailService(NotificationRepository notificationRepository, JavaMailSender mailSender) {
        this.notificationRepository = notificationRepository;
        this.mailSender = mailSender;
    }

    /**
     * Sends a general email asynchronously.
     *
     * @param to      recipient's email
     * @param body    email content (HTML supported)
     * @param subject subject of the email
     */
    @Async
    public void sendEmail(String to, String body, String subject) {
        sendEmail(to, body, subject, null); // Default 'from' to null
    }

    /**
     * Sends a general email asynchronously.
     *
     * @param to      recipient's email
     * @param body    email content (HTML supported)
     * @param subject subject of the email
     * @param from    sender's email address (overrides default if provided)
     */
    @Async
    public void sendEmail(String to, String body, String subject, String from) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

            helper.setText(body, true); // true = enable HTML content
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setFrom(from != null ? from : domainName); // Use provided sender or default domain

            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            LOGGER.error("Failed to send email", e);
            throw new IllegalStateException("Failed to send email", e);
        }
    }

    /**
     * Sends a notification email and updates notification status.
     *
     * @param to           recipient's email
     * @param email        email content
     * @param subject      subject of the email
     * @param notification notification entity to update status
     */
    @Override
    @Async
    public void send(String to, String email, String subject, Notifications notification) {
        try {
            sendEmail(to, email, subject); // Reuse sendEmail method for notifications

            // Update notification status to SENT
            notification.setNotificationStatus(NotificationStatus.SENT);
            notificationRepository.save(notification);
        } catch (Exception e) {
            LOGGER.error("Failed to send notification email", e);

            // Update notification status to FAILED
            notification.setNotificationStatus(NotificationStatus.FAILED);
            notificationRepository.save(notification);

            throw new IllegalStateException("Failed to send notification email", e);
        }
    }
}
