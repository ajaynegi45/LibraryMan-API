package com.libraryman_api.newsletter;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "newsletter_subscribers")
public class NewsletterSubscriber {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private boolean active = true; // Manage subscription status

    @Column(name = "unsubscribe_token", nullable = false, unique = true)
    private String unsubscribeToken; // Token for unsubscribing

    // Default constructor that initializes the token
    public NewsletterSubscriber() {
        this.unsubscribeToken = UUID.randomUUID().toString(); // Generate token by default
    }

    // Constructor to initialize with email
    public NewsletterSubscriber(String email) {
        this();
        this.email = email;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getUnsubscribeToken() {
        return unsubscribeToken;
    }

    // Method to regenerate a new token
    public void regenerateToken() {
        this.unsubscribeToken = UUID.randomUUID().toString();
    }
}
