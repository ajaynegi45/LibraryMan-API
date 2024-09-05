package com.libraryman_api.email;

import com.libraryman_api.entity.Notifications;

/**
 * Interface representing an email sending service for the Library Management System.
 * Classes implementing this interface are responsible for sending emails
 * and updating the status of notifications in the system.
 */
public interface EmailSender {

    /**
     * Sends an email to the specified recipient with the given body, subject, and notification details.
     *
     * @param to           the email address of the recipient
     * @param body         the body of the email, typically in HTML or plain text format
     * @param subject      the subject of the email
     * @param notification the notification entity associated with the email being sent,
     *                     used to track the status of the notification
     */
    void send(String to, String body, String subject, Notifications notification);
}
