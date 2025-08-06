package com.libraryman_api.notification;

import com.libraryman_api.TestUtil;
import com.libraryman_api.borrowing.Borrowings;
import com.libraryman_api.email.EmailSender;
import com.libraryman_api.member.MemberRepository;
import com.libraryman_api.member.Members;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests the {@link NotificationService}.
 */
@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {
    @Mock
    private EmailSender emailSender;
    @Mock
    private NotificationRepository notificationRepository;
    @Mock
    private MemberRepository memberRepository;
    @InjectMocks
    private NotificationService notificationService;

    @Captor
    private ArgumentCaptor<String> emailToCaptor;
    @Captor
    private ArgumentCaptor<String> emailBodyCaptor;
    @Captor
    private ArgumentCaptor<String> emailSubjectCaptor;
    @Captor
    private ArgumentCaptor<Notifications> notificationCaptor;

    @Test
    void accountCreatedNotification() {
        Members members = TestUtil.getMembers();

        notificationService.accountCreatedNotification(members);

        verifyEmailSent(members.getEmail(), "We’re excited to welcome you to LibraryMan", "Welcome to LibraryMan!", NotificationType.ACCOUNT_CREATED);
        verify(notificationRepository).save(notificationCaptor.getValue());
    }

    @Test
    void accountDeletionNotification() {
        Members members = TestUtil.getMembers();

        notificationService.accountDeletionNotification(members);

        verifyEmailSent(members.getEmail(), "We’re sorry to see you go!", "Your LibraryMan Account has been Deleted", NotificationType.ACCOUNT_DELETED);
    }

    @Test
    void borrowBookNotification() {
        Borrowings borrowings = TestUtil.getBorrowings();

        notificationService.borrowBookNotification(borrowings);

        verifyEmailSent(borrowings.getMember().getEmail(), "You have successfully borrowed", "Book Borrowed Successfully", NotificationType.BORROW);
        verify(notificationRepository).save(notificationCaptor.getValue());
    }

    @Test
    void reminderNotification() {
        Borrowings borrowings = TestUtil.getBorrowings();

        notificationService.reminderNotification(borrowings);

        verifyEmailSent(borrowings.getMember().getEmail(), "This is a friendly reminder that the due date to return", "Reminder: Book Due Date Approaching", NotificationType.REMINDER);
        verify(notificationRepository).save(notificationCaptor.getValue());
    }

    @Test
    void finePaidNotification() {
        Borrowings borrowings = TestUtil.getBorrowings();
        borrowings.setFine(TestUtil.getFine());

        notificationService.finePaidNotification(borrowings);

        verifyEmailSent(borrowings.getMember().getEmail(), "Thank you for your payment.", "Payment Received", NotificationType.PAID);
        verify(notificationRepository).save(notificationCaptor.getValue());
    }

    @Test
    void fineImposedNotification() {
        Borrowings borrowings = TestUtil.getBorrowings();
        borrowings.setFine(TestUtil.getFine());

        notificationService.fineImposedNotification(borrowings);

        verifyEmailSent(borrowings.getMember().getEmail(), "Unfortunately, our records show that the book was returned after the due date", "Fine Imposed for Late Return", NotificationType.FINE);
        verify(notificationRepository).save(notificationCaptor.getValue());
    }

    @Test
    void accountDetailsUpdateNotification() {
        Members members = TestUtil.getMembers();

        notificationService.accountDetailsUpdateNotification(members);

        verifyEmailSent(members.getEmail(), "Your account details have been successfully updated", "Your Account Details Have Been Updated", NotificationType.UPDATE);
        verify(notificationRepository).save(notificationCaptor.getValue());
    }

    @Test
    void bookReturnedNotification() {
        Borrowings borrowings = TestUtil.getBorrowings();
        borrowings.setReturnDate(new Date());

        notificationService.bookReturnedNotification(borrowings);

        verifyEmailSent(borrowings.getMember().getEmail(), "Thank you for returning", "Thank You for Returning Your Book", NotificationType.RETURNED);
        verify(notificationRepository).save(notificationCaptor.getValue());
    }

    @Nested
    class SendDueDateReminders {
        @Test
        void success() {
            Borrowings borrowings = TestUtil.getBorrowings();
            List<Borrowings> borrowingsList = List.of(borrowings);
            when(notificationRepository.findBorrowingsDueInDays(any(), any())).thenReturn(borrowingsList);
            when(memberRepository.findByMemberId(anyInt())).thenReturn(Optional.of(borrowings.getMember()));

            notificationService.sendDueDateReminders();

            verifyEmailSent(borrowings.getMember().getEmail(), "This is a friendly reminder that the due date to return", "Reminder: Book Due Date Approaching", NotificationType.REMINDER);
            verify(notificationRepository).save(notificationCaptor.getValue());
        }

        @Test
        void noBooksDueSoon() {
            when(notificationRepository.findBorrowingsDueInDays(any(), any())).thenReturn(List.of());

            notificationService.sendDueDateReminders();

            verify(emailSender, never()).send(any(), any(), any(), any());
        }

        @Test
        void memberNotFound() {
            Borrowings borrowings = TestUtil.getBorrowings();
            List<Borrowings> borrowingsList = List.of(borrowings);
            when(notificationRepository.findBorrowingsDueInDays(any(), any())).thenReturn(borrowingsList);
            when(memberRepository.findByMemberId(anyInt())).thenReturn(Optional.empty());

            notificationService.sendDueDateReminders();

            verify(emailSender, never()).send(any(), any(), any(), any());
        }
    }

    private void verifyEmailSent(String expectedToEmail, String expectedPartOfBodyText, String expectedSubjectText, NotificationType expectedNotificationType) {
        verify(emailSender).send(emailToCaptor.capture(), emailBodyCaptor.capture(), emailSubjectCaptor.capture(), notificationCaptor.capture());
        assertEquals(expectedToEmail, emailToCaptor.getValue());
        assertTrue(emailBodyCaptor.getValue().contains(expectedPartOfBodyText));
        assertEquals(expectedSubjectText, emailSubjectCaptor.getValue());
        assertEquals(expectedNotificationType, notificationCaptor.getValue().getNotificationType());
    }
}
