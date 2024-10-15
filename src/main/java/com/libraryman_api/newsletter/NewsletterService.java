package com.libraryman_api.newsletter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class NewsletterService {

    @Autowired
    private NewsletterSubscriberRepository subscriberRepository;

    public String subscribe(String email) {
        // Validate email format
        if (!isValidEmail(email)) {
            return "Invalid email format.";
        }

        // Check if the email already exists
        if (subscriberRepository.findByEmail(email).isPresent()) {
            return "Email already subscribed.";
        }

        // Save new subscriber
        NewsletterSubscriber subscriber = new NewsletterSubscriber(email);
        subscriberRepository.save(subscriber);
        return "You have successfully signed up for the newsletter!"; // Confirmation message
    }

    public boolean isValidEmail(String email) {
        if (email == null) return false; // Handle null case
        email = email.trim();
        // Regular expression for validating an email address
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
