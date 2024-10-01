package com.libraryman_api.email;

import com.libraryman_api.notification.NotificationStatus;
import com.libraryman_api.notification.Notifications;
import com.libraryman_api.notification.NotificationRepository;
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
 * Service class responsible for sending emails asynchronously in the LibraryMan system.
 * 
 * <p>This service manages the creation and sending of MIME email messages, as well as updating the 
 * status of notifications within the system. It ensures that emails are sent in the background 
 * using asynchronous execution, allowing the main thread to remain responsive.</p>
 *
 * <p>In the case of a failed email sending operation, the service logs the error, updates 
 * the notification status to {@link NotificationStatus#FAILED}, and throws an {@link IllegalStateException}.</p>
 *
 * @see NotificationRepository
 * @see JavaMailSender
 * @see Notifications
 * @see NotificationStatus
 */
@Service
public class EmailService implements EmailSender {

    private final NotificationRepository notificationRepository;
    private final JavaMailSender mailSender;
    private static final Logger LOGGER = LoggerFactory.getLogger(EmailService.class);

    @Value("${spring.mail.properties.domain_name}")
    private String domainName;

    /**
     * Constructs a new {@code EmailService} instance, injecting the necessary dependencies 
     * for sending emails and managing notification entities.
     *
     * @param notificationRepository the repository for storing and managing email notifications
     * @param mailSender the {@link JavaMailSender} used for sending emails
     */
    public EmailService(NotificationRepository notificationRepository, JavaMailSender mailSender) {
        this.notificationRepository = notificationRepository;
        this.mailSender = mailSender;
    }

    /**
     * Sends an email asynchronously to a specified recipient.
     * 
     * <p>This method creates a MIME message and sends it to the recipient using 
     * the configured {@link JavaMailSender}. If the email is successfully sent, the 
     * notification's status is updated to {@link NotificationStatus#SENT}. In case of a failure, 
     * the status is updated to {@link NotificationStatus#FAILED} and an error is logged.</p>
     * 
     * <p>This method runs asynchronously, meaning the main application flow is not blocked 
     * while the email is being processed and sent.</p>
     *
     * @param to the recipient's email address
     * @param email the body of the email to be sent (HTML content supported)
     * @param subject the subject line of the email
     * @param notification the {@link Notifications} object representing the email notification in the system
     * @throws IllegalStateException if the email fails to send due to a {@link MessagingException}
     */
    @Override
    @Async
    public void send(String to, String email, String subject, Notifications notification) {
        try {
            // Construct the email message
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(email, true); // Set email body (HTML enabled)
            helper.setTo(to); // Set recipient email
            helper.setSubject(subject); // Set subject
            helper.setFrom(domainName); // Set sender's email (from domain)

            // Send the email
            mailSender.send(mimeMessage);

            // Update notification status to SENT
            notification.setNotificationStatus(NotificationStatus.SENT);
            notificationRepository.save(notification);
        } catch (MessagingException e) {
            // Log the failure and update notification status to FAILED
            LOGGER.error("Failed to send email to {}", to, e);
            notification.setNotificationStatus(NotificationStatus.FAILED);
            notificationRepository.save(notification);

            // Throw an exception to signal the failure
            throw new IllegalStateException("Failed to send email", e);
        }
    }
}
