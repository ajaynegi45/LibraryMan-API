package com.libraryman_api.entity;


/**
 * Represents the types of notifications available in the system.
 */
public enum NotificationType {
    /** Used for account creation notifications. */
    ACCOUNT_CREATED,

    /** Used for account deletion notifications. */
    ACCOUNT_DELETED,

    /** Sent when a user borrows a book. */
    BORROW,

    /** Sent to remind user for due date */
    REMINDER,

    /** Indicates that a user has paid a fine. */
    PAID,

    /** Imposed when a user incurs a fine. */
    FINE,

    /** Used when updating user details. */
    UPDATE,

    /** Sent when a user returns a borrowed book. */
    RETURNED
}
