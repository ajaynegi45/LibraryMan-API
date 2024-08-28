package com.libraryman_api.service;

import com.libraryman_api.entity.*;
import com.libraryman_api.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;


@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }


    public void sendReminderNotification(Borrowings borrowing) {
        Notifications notification = new Notifications();
        notification.setMember(borrowing.getMember()); // Set the Members object instead of the memberId
        notification.setMessage("Your borrowed book is due on " + borrowing.getDueDate());
        notification.setNotificationType(NotificationType.REMINDER);
        notification.setSentDate(new Timestamp(System.currentTimeMillis()));
        notificationRepository.save(notification);
    }

    public void sendFineNotification(Members member) {
        Notifications notification = new Notifications();
        notification.setMember(member); // Set the Members object instead of the memberId
        notification.setMessage("A fine has been imposed for overdue book return.");
        notification.setNotificationType(NotificationType.FINE);
        notification.setSentDate(new Timestamp(System.currentTimeMillis()));
        notificationRepository.save(notification);
    }



}

