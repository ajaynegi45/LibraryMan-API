package com.libraryman_api.entity;

import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;


import java.sql.Timestamp;


@Entity
public class Notifications {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private int notificationId;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Members member;

    @Column(nullable = false)
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(name = "notification_type", nullable = false)
    private NotificationType notificationType;

    @Column(name = "sent_date", nullable = false)
    private Timestamp sentDate;

    private String status;



    public Notifications() {
    }

    public Notifications( Members member, String message, NotificationType notificationType, Timestamp sentDate, String status) {
        this.member = member;
        this.message = message;
        this.notificationType = notificationType;
        this.sentDate = sentDate;
        this.status = status;
    }

    public int getNotificationId() {
        return notificationId;
    }

    public Members getMember() {
        return member;
    }

    public void setMember(Members member) {
        this.member = member;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public NotificationType getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(NotificationType notificationType) {
        this.notificationType = notificationType;
    }

    public Timestamp getSentDate() {
        return sentDate;
    }

    public void setSentDate(Timestamp sentDate) {
        this.sentDate = sentDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
