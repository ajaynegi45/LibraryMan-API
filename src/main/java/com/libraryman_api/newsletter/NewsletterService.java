package com.libraryman_api.newsletter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class NewsletterService {

    @Autowired
    private NewsletterSubscriberRepository subscriberRepository;

    // Subscribe user after validating email
    public String subscribe(String email) {
        if (!isValidEmail(email)) {
            return "Invalid email format.";
        }

        // Check if the email already exists
        Optional<NewsletterSubscriber> optionalSubscriber = subscriberRepository.findByEmail(email);

        if (optionalSubscriber.isPresent()) {
            NewsletterSubscriber subscriber = optionalSubscriber.get();

            // If the subscriber is inactive, reactivate them
            if (!subscriber.isActive()) {
                subscriber.setActive(true); // Reactivate the subscriber
                subscriber.regenerateToken(); // Generate a new token
                subscriberRepository.save(subscriber); // Save the updated subscriber
                return "You have successfully re-subscribed!";
            } else {
                return "Email is already subscribed.";
            }
        }

        // Save new subscriber if not present
        NewsletterSubscriber subscriber = new NewsletterSubscriber(email);
        subscriberRepository.save(subscriber);
        return "You have successfully subscribed!";
    }

    // Unsubscribe user using the token
    public String unsubscribe(String token) {
        Optional<NewsletterSubscriber> optionalSubscriber = subscriberRepository.findByUnsubscribeToken(token);

        if (optionalSubscriber.isEmpty()) {
            return "Invalid or expired token.";
        }

        NewsletterSubscriber subscriber = optionalSubscriber.get();

        if (!subscriber.isActive()) {
            return "You are already unsubscribed.";
        }

        subscriber.setActive(false); // Set active to false
        subscriberRepository.save(subscriber); // Save the updated subscriber
        return "You have successfully unsubscribed!";
    }

    // Email validation logic
    private boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
