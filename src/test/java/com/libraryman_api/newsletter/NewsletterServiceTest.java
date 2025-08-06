package com.libraryman_api.newsletter;

import com.libraryman_api.TestUtil;
import com.libraryman_api.email.EmailService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Tests the {@link NewsletterService}.
 */
@ExtendWith(MockitoExtension.class)
class NewsletterServiceTest {
    @Mock
    private NewsletterSubscriberRepository subscriberRepository;
    @Mock
    private EmailService emailService;
    @InjectMocks
    private NewsletterService newsletterService;

    @Nested
    class Subscribe {
        @Test
        void success() {
            NewsletterSubscriber newsletterSubscriber = TestUtil.getNewsletterSubscriber();
            String email = newsletterSubscriber.getEmail();
            when(subscriberRepository.findByEmail(any())).thenReturn(Optional.of(newsletterSubscriber));

            String result = newsletterService.subscribe(email);

            assertEquals("You have successfully re-subscribed!", result);
            verify(subscriberRepository).save(newsletterSubscriber);
            verify(emailService).sendEmail(eq(email), any(), any());
        }

        @Test
        void alreadySubscribed() {
            NewsletterSubscriber newsletterSubscriber = TestUtil.getNewsletterSubscriber();
            newsletterSubscriber.setActive(true);
            String email = newsletterSubscriber.getEmail();
            when(subscriberRepository.findByEmail(any())).thenReturn(Optional.of(newsletterSubscriber));

            String result = newsletterService.subscribe(email);

            assertEquals("Email is already subscribed.", result);
            verify(subscriberRepository, never()).save(newsletterSubscriber);
            verify(emailService, never()).sendEmail(eq(email), any(), any());
        }

        @Test
        void noSubscriberFound() {
            String email = "wendy@outlook.com";
            when(subscriberRepository.findByEmail(any())).thenReturn(Optional.empty());

            String result = newsletterService.subscribe(email);

            assertEquals("You have successfully subscribed!", result);
            verify(subscriberRepository).save(any());
            verify(emailService).sendEmail(eq(email), any(), any());
        }

        @Test
        void invalidEmail() {
            String email = "wendy@outlookcom";

            String result = newsletterService.subscribe(email);

            assertEquals("Invalid email format.", result);
        }
    }

    @Nested
    class Unsubscribe {
        @Test
        void success() {
            NewsletterSubscriber newsletterSubscriber = TestUtil.getNewsletterSubscriber();
            newsletterSubscriber.setActive(true);
            String token = newsletterSubscriber.getUnsubscribeToken();
            when(subscriberRepository.findByUnsubscribeToken(any())).thenReturn(Optional.of(newsletterSubscriber));

            String result = newsletterService.unsubscribe(token);

            assertEquals("You have successfully unsubscribed!", result);
            verify(subscriberRepository).save(newsletterSubscriber);
            verify(emailService).sendEmail(eq(newsletterSubscriber.getEmail()), any(), any());
        }

        @Test
        void invalidToken() {
            when(subscriberRepository.findByUnsubscribeToken(any())).thenReturn(Optional.empty());

            String result = newsletterService.unsubscribe("token");

            assertEquals("Invalid or expired token.", result);
            verify(subscriberRepository, never()).save(any());
            verify(emailService, never()).sendEmail(any(), any(), any());
        }

        @Test
        void alreadyUnsubscribed() {
            NewsletterSubscriber newsletterSubscriber = TestUtil.getNewsletterSubscriber();
            newsletterSubscriber.setActive(false);
            String token = newsletterSubscriber.getUnsubscribeToken();
            when(subscriberRepository.findByUnsubscribeToken(any())).thenReturn(Optional.of(newsletterSubscriber));

            String result = newsletterService.unsubscribe(token);

            assertEquals("You are already unsubscribed.", result);
            verify(subscriberRepository, never()).save(any());
            verify(emailService, never()).sendEmail(any(), any(), any());
        }
    }
}
