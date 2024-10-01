package com.libraryman_api.email;

import com.libraryman_api.notification.Notifications;

/**
 * Interface representing an email sending service for the LibraryMan system.
 * 
 * <p>This interface defines the contract for sending emails asynchronously to recipients.
 * Implementing classes are responsible for constructing and sending the email, as well as 
 * updating the status of notifications (represented by {@link Notifications}) within the system.</p>
 *
 * <p>The email sending process involves constructing an email with a subject, body, and recipient 
 * details, and ensuring that the notification entity is updated based on whether the email 
 * was successfully sent or encountered an error.</p>
 *
 * <p>Classes implementing this interface typically use a mailing service such as 
 * {@link org.springframework.mail.javamail.JavaMailSender} to perform the actual email delivery.</p>
 *
 * @see Notifications
 */
public interface EmailSender {

    /**
     * Sends an email asynchronously to the specified recipient with the provided content and subject.
     * 
     * <p>This method is responsible for constructing the email message, sending it to the recipient,
     * and updating the corresponding notification status within the system based on the outcome.
     * The email body can be in HTML or plain text format, and the associated {@link Notifications} entity
     * tracks the status of the email (e.g., SENT or FAILED).</p>
     * 
     * <p>In case of errors during the sending process, the implementation of this method should handle 
     * the exceptions and log any failures accordingly. The notification status should be updated to reflect 
     * the success or failure of the email operation.</p>
     * 
     * @param to           the email address of the recipient
     * @param body         the content of the email, either in HTML or plain text format
     * @param subject      the subject line of the email
     * @param notification the notification entity representing the email notification in the system, 
     *                     used to track the status (SENT or FAILED) of the email being sent
     * @throws IllegalStateException if there is an issue sending the email
     */
    void send(String to, String body, String subject, Notifications notification);
}
