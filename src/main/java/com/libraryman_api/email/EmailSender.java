package com.libraryman_api.email;
import com.libraryman_api.entity.Notifications;

public interface EmailSender {
    void send (String to, String body, String subject, Notifications notification);
}
