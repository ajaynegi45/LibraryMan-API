package com.libraryman_api.email;

import com.libraryman_api.entity.NotificationStatus;
import com.libraryman_api.entity.Notifications;
import com.libraryman_api.repository.NotificationRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


@Service
public class EmailService implements EmailSender{

    private final NotificationRepository notificationRepository;
    private final JavaMailSender mailSender;
    public EmailService(NotificationRepository notificationRepository, JavaMailSender mailSender) {
        this.notificationRepository = notificationRepository;
        this.mailSender = mailSender;
    }
    private final static Logger LOGGER = LoggerFactory
            .getLogger(EmailService.class);

    @Value("${spring.mail.properties.domain_name}")
    private String domainName;


    @Override
    @Async
    public  void send(String to, String email, String subject, Notifications notification) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(email, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setFrom(domainName);
            mailSender.send(mimeMessage);

            notification.setNotificationStatus(NotificationStatus.SENT);
            notificationRepository.save(notification);
        } catch (MessagingException e) {
            LOGGER.error("failed to send email", e);
            notification.setNotificationStatus(NotificationStatus.FAILED);
            notificationRepository.save(notification);
            throw new IllegalStateException("failed to send email");
        }
    }
}
