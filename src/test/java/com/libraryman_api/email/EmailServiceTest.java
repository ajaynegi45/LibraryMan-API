package com.libraryman_api.email;

import com.libraryman_api.notification.NotificationRepository;
import com.libraryman_api.notification.NotificationStatus;
import com.libraryman_api.notification.Notifications;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests the {@link EmailService}.
 */
@ExtendWith(MockitoExtension.class)
class EmailServiceTest {
    @Mock
    private NotificationRepository notificationRepository;
    @Mock
    private JavaMailSender mailSender;
    @InjectMocks
    private EmailService emailService;

    @Captor
    private ArgumentCaptor<Notifications> notificationCaptor;
    @Captor
    private ArgumentCaptor<MimeMessage> mimeMessageCaptor;

    @BeforeEach
    void setup() throws Exception {
        injectPropertyValue(emailService);
        when(mailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));
    }

    private void injectPropertyValue(Object emailService) throws Exception {
        Field field = emailService.getClass().getDeclaredField("domainName");
        field.setAccessible(true);
        field.set(emailService, "default.io");
    }

    @Nested
    class SendEmail {
        @Test
        void withFrom() throws MessagingException {
            emailService.sendEmail("to@example.com", "<p>Hello</p>", "Subject", "sender@libraryman.com");

            verify(mailSender).send(mimeMessageCaptor.capture());
            MimeMessage sentMessage = mimeMessageCaptor.getValue();
            assertEquals("to@example.com", sentMessage.getRecipients(Message.RecipientType.TO)[0].toString());
            assertEquals("Subject", sentMessage.getSubject());
            assertEquals("sender@libraryman.com", sentMessage.getFrom()[0].toString());
        }

        @Test
        void withoutFrom_usesDefaultDomain() throws MessagingException {
            emailService.sendEmail("to@example.com", "<p>Hello</p>", "Subject");

            verify(mailSender).send(mimeMessageCaptor.capture());
            MimeMessage sentMessage = mimeMessageCaptor.getValue();
            assertEquals("to@example.com", sentMessage.getRecipients(Message.RecipientType.TO)[0].toString());
            assertEquals("Subject", sentMessage.getSubject());
            assertEquals("default.io", sentMessage.getFrom()[0].toString());
        }

        @Test
        void invalidToEmail_throwsException() {
            String invalidToEmail = "";

            IllegalStateException ex = assertThrows(IllegalStateException.class, () -> emailService.sendEmail(invalidToEmail, "Body", "Subject", "sender@libraryman.com"));

            assertTrue(ex.getMessage().contains("Failed to send email"));
        }
    }

    @Nested
    class Send {
        @Test
        void success() {
            emailService.send("to@example.com", "<p>Hello</p>", "Subject", new Notifications());

            verify(notificationRepository).save(notificationCaptor.capture());
            assertEquals(NotificationStatus.SENT, notificationCaptor.getValue().getNotificationStatus());
        }

        @Test
        void invalidToEmail_updatesStatusToFailed() {
            String invalidToEmail = "";

            IllegalStateException ex = assertThrows(IllegalStateException.class, () -> emailService.send(invalidToEmail, "Email", "Subject", new Notifications()));

            assertTrue(ex.getMessage().contains("Failed to send notification email"));
            verify(notificationRepository).save(notificationCaptor.capture());
            assertEquals(NotificationStatus.FAILED, notificationCaptor.getValue().getNotificationStatus());
        }
    }
}
