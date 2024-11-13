package com.libraryman_api.newsletter;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NewsletterSubscriberRepository extends JpaRepository<NewsletterSubscriber, Long> {
    Optional<NewsletterSubscriber> findByEmail(String email);

    Optional<NewsletterSubscriber> findByUnsubscribeToken(String unsubscribeToken);
}
