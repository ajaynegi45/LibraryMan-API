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
    private boolean active = true;

    @Column(name = "unsubscribe_token", nullable = false, unique = true)
    private String unsubscribeToken;

    // Default constructor initializing unsubscribe token
    public NewsletterSubscriber() {
        this.unsubscribeToken = UUID.randomUUID().toString();
    }

    // Constructor initializing with email
    public NewsletterSubscriber(String email) {
        this();
        this.email = email;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public String getUnsubscribeToken() { return unsubscribeToken; }
    public void regenerateToken() { this.unsubscribeToken = UUID.randomUUID().toString(); }
}
