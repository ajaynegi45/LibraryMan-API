package com.libraryman_api.email;

import com.libraryman_api.notification.NotificationStatus;
import com.libraryman_api.notification.Notifications;
import com.libraryman_api.notification.NotificationRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Service class for sending emails asynchronously.
 * This class handles the construction and sending of MIME email messages.
 */
@Service
public class EmailService implements EmailSender {

    private final NotificationRepository notificationRepository;
    private final JavaMailSender mailSender;

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailService.class);

    @Value("${spring.mail.properties.domain_name}")
    private String domainName;

    /**
     * Constructs a new {@code EmailService} with the specified {@link NotificationRepository} and {@link JavaMailSender}.
     *
     * @param notificationRepository the repository for managing notification entities
     * @param mailSender the mail sender for sending email messages
     */
    public EmailService(NotificationRepository notificationRepository, JavaMailSender mailSender) {
        this.notificationRepository = notificationRepository;
        this.mailSender = mailSender;
    }

    /**
     * Sends an email asynchronously to the specified recipient.
     * If the email is successfully sent, the notification status is updated to SENT.
     * If the email fails to send, the notification status is updated to FAILED and an exception is thrown.
     *
     * @param to the recipient's email address
     * @param email the content of the email to send
     * @param subject the subject of the email
     * @param notification the {@link Notifications} object representing the email notification
     */
    @Override
    @Async
    public void send(String to, String email, String subject, Notifications notification) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(email, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setFrom(domainName);
            mailSender.send(mimeMessage);

            // Update notification status to SENT
            notification.setNotificationStatus(NotificationStatus.SENT);
            notificationRepository.save(notification);
        } catch (MessagingException e) {
            LOGGER.error("Failed to send email", e);

            // Update notification status to FAILED
            notification.setNotificationStatus(NotificationStatus.FAILED);
            notificationRepository.save(notification);

            throw new IllegalStateException("Failed to send email", e);
        }
    }
}
