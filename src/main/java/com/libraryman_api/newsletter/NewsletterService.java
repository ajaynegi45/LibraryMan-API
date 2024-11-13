package com.libraryman_api.newsletter;

import com.libraryman_api.email.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class NewsletterService {

    private final NewsletterSubscriberRepository subscriberRepository;
    private final EmailService emailService;

    @Autowired
    public NewsletterService(NewsletterSubscriberRepository subscriberRepository, EmailService emailService) {
        this.subscriberRepository = subscriberRepository;
        this.emailService = emailService;
    }

    public String subscribe(String email) {
        if (!isValidEmail(email)) return "Invalid email format.";

        Optional<NewsletterSubscriber> optionalSubscriber = subscriberRepository.findByEmail(email);
        if (optionalSubscriber.isPresent()) {
            NewsletterSubscriber subscriber = optionalSubscriber.get();
            if (!subscriber.isActive()) {
                subscriber.setActive(true);
                subscriber.regenerateToken();
                subscriberRepository.save(subscriber);
                sendSubscriptionEmail(email, subscriber.getUnsubscribeToken());
                return "You have successfully re-subscribed!";
            }
            return "Email is already subscribed.";
        }

        NewsletterSubscriber newSubscriber = new NewsletterSubscriber(email);
        subscriberRepository.save(newSubscriber);
        sendSubscriptionEmail(email, newSubscriber.getUnsubscribeToken());
        return "You have successfully subscribed!";
    }

    public String unsubscribe(String token) {
        Optional<NewsletterSubscriber> optionalSubscriber = subscriberRepository.findByUnsubscribeToken(token);
        if (optionalSubscriber.isEmpty()) return "Invalid or expired token.";

        NewsletterSubscriber subscriber = optionalSubscriber.get();
        if (!subscriber.isActive()) return "You are already unsubscribed.";

        subscriber.setActive(false);
        subscriberRepository.save(subscriber);
        sendUnsubscribeEmail(subscriber.getEmail());
        return "You have successfully unsubscribed!";
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return Pattern.compile(emailRegex).matcher(email).matches();
    }

    private void sendSubscriptionEmail(String email, String token) {
        String subject = "Welcome to Our Newsletter!";
        String body = "Thank you for subscribing! " +
                "To unsubscribe, click the link:\n" +
                "http://localhost:8080/api/newsletter/unsubscribe?token=" + token;

        emailService.sendEmail(email, body, subject); // No need to change this line
    }

    private void sendUnsubscribeEmail(String email) {
        String subject = "You have been unsubscribed";
        String body = "You have successfully unsubscribed. If this was a mistake, you can re-subscribe.";

        emailService.sendEmail(email, body, subject); // No need to change this line
    }
}
