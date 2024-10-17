package com.libraryman_api.newsletter;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NewsletterSubscriberRepository extends JpaRepository<NewsletterSubscriber, Long> {

    // Find a subscriber by email
    Optional<NewsletterSubscriber> findByEmail(String email);

    // Find a subscriber by unsubscribe token
    Optional<NewsletterSubscriber> findByUnsubscribeToken(String unsubscribeToken);
}
