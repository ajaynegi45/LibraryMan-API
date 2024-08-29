package com.libraryman_api.service;

import com.libraryman_api.email.EmailSender;
import com.libraryman_api.entity.*;
import com.libraryman_api.exception.ResourceNotFoundException;
import com.libraryman_api.repository.MemberRepository;
import com.libraryman_api.repository.NotificationRepository;
import org.springframework.stereotype.Service;
import java.sql.Timestamp;


@Service
public class NotificationService {
    private final EmailSender emailSender;
    private final NotificationRepository notificationRepository;
    private final MemberRepository memberRepository;
    public NotificationService(EmailSender emailSender, NotificationRepository notificationRepository, MemberRepository memberRepository) {
        this.emailSender = emailSender;
        this.notificationRepository = notificationRepository;
        this.memberRepository = memberRepository;
    }



    public void accountCreatedNotification(Members members) {
        Notifications notification = new Notifications();
        notification.setMember(members);
        notification.setMessage("We’re excited to welcome you to LibraryMan! Your account has been successfully created, and you’re now part of our community of book lovers. \uD83D\uDCDA<br><br>Feel free to explore our vast collection of books and other resources. If you have any questions or need assistance, our team is here to help.<br><br>Happy reading!  \uD83D\uDCD6");
        notification.setNotificationType(NotificationType.ACCOUNT_CREATED);
        notification.setSentDate(new Timestamp(System.currentTimeMillis()));

        sendNotification(notification);
        notificationRepository.save(notification);
    }

    public void accountDeletionNotification(Members members) {
        Notifications notification = new Notifications();
        notification.setMember(members);
        notification.setMessage("We’re sorry to see you go! Your account with LibraryMan has been successfully deleted as per your request.<br><br>If you change your mind in the future, you’re always welcome to create a new account with us. Should you have any questions or concerns, please don’t hesitate to reach out.<br><br>Thank you for being a part of our community.");
        notification.setNotificationType(NotificationType.ACCOUNT_DELETED);
        notification.setSentDate(new Timestamp(System.currentTimeMillis()));
        sendNotification(notification);
    }


    public void borrowBookNotification(Borrowings borrowing) {
        Notifications notification = new Notifications();
        notification.setMember(borrowing.getMember());
        notification.setMessage("Congratulations! \uD83C\uDF89 You have successfully borrowed '" +
                borrowing.getBook().getTitle() + " book on " + // This is passing null value. correct it
                borrowing.getBorrowDate() +
                ".<br><br>You now have 15 days to enjoy reading it. We kindly request that you return it to us on or before " +
                borrowing.getDueDate() +
                " to avoid any late fees \uD83D\uDCC6, which are ₹10 per day for late returns.<br><br>If you need to renew the book or have any questions, please don't hesitate to reach out to us.<br><br>Thank you for choosing our library!");
        notification.setNotificationType(NotificationType.BORROW);
        notification.setSentDate(new Timestamp(System.currentTimeMillis()));

        sendNotification(notification);
        notificationRepository.save(notification);
    }


    public void reminderNotification(Borrowings borrowing) {
        Notifications notification = new Notifications();
        notification.setMember(borrowing.getMember());
        notification.setMessage("This is a friendly reminder that the due date to return '" +
                borrowing.getBook().getTitle()  + // This is passing null value. correct it
                "' book is approaching. Please ensure that you return the book by " +
                borrowing.getBorrowDate() +
                " to avoid any late fees. \uD83D\uDCC5" +
                "<br><br>If you need more time, consider renewing your book through our online portal or by contacting us." +
                "<br><br>Thank you, and happy reading! \uD83D\uDE0A");
        notification.setNotificationType(NotificationType.REMINDER);
        notification.setSentDate(new Timestamp(System.currentTimeMillis()));

        sendNotification(notification);
        notificationRepository.save(notification);
    }

    public void finePaidNotification(Borrowings borrowing) {
        Notifications notification = new Notifications();
        notification.setMember(borrowing.getMember());
        notification.setMessage("Thank you for your payment. We’ve received your payment of ₹" +
                borrowing.getFine().getAmount() + " towards the fine for the overdue return of '" + borrowing.getBook().getTitle()  + "' book. ✅" +
                "<br><br>Your account has been updated accordingly. If you have any questions or need further assistance, please feel free to reach out.\n" +
                "<br><br>Thank you for your prompt payment.");
        notification.setNotificationType(NotificationType.PAID);
        notification.setSentDate(new Timestamp(System.currentTimeMillis()));

        sendNotification(notification);
        notificationRepository.save(notification);
    }


    public void fineImposedNotification(Borrowings borrowing) {
        Notifications notification = new Notifications();
        notification.setMember(borrowing.getMember());
        notification.setMessage("We hope you enjoyed reading '" +
                borrowing.getBook().getTitle() +
                "' book. Unfortunately, our records show that the book was returned after the due date of " +
                borrowing.getDueDate() +
                " . As a result, a fine of ₹10 per day has been imposed for the late return.<br><br>The total fine amount for this overdue return is ₹" +
                borrowing.getFine().getAmount() +
                ".<br><br>If you have any questions or would like to discuss this matter further, please don't hesitate to contact us.<br><br>Thank you for your understanding and for being a valued member of our library.");
        notification.setNotificationType(NotificationType.FINE);
        notification.setSentDate(new Timestamp(System.currentTimeMillis()));

        sendNotification(notification);
        notificationRepository.save(notification);
    }


    public void accountDetailsUpdateNotification(Members members) {
        Notifications notification = new Notifications();
        notification.setMember(members);
        notification.setMessage("Your account details have been successfully updated as per your request. If you did not authorize this change or if you notice any discrepancies, please contact us immediately." +
                "<br><br>Thank you for keeping your account information up to date.");
        notification.setNotificationType(NotificationType.UPDATE);
        notification.setSentDate(new Timestamp(System.currentTimeMillis()));

        sendNotification(notification);
        notificationRepository.save(notification);
    }

    public void bookReturnedNotification(Borrowings borrowing) {
        Notifications notification = new Notifications();
        notification.setMember(borrowing.getMember());
        notification.setMessage("Thank you for returning '" + borrowing.getBook().getTitle() + "' book on " + borrowing.getReturnDate() + ". We hope you enjoyed the book!" +
                "<br><br>Feel free to explore our collection for your next read. If you have any questions or need assistance, we’re here to help." +
                "<br><br>Thank you for choosing LibraryMan!");
        notification.setNotificationType(NotificationType.RETURNED);
        notification.setSentDate(new Timestamp(System.currentTimeMillis()));

        sendNotification(notification);
        notificationRepository.save(notification);
    }


    private void sendNotification(Notifications notification) {
        Members member = memberRepository.findByMemberId(notification.getMember().getMemberId())
                .orElseThrow(() -> new ResourceNotFoundException("Member not found"));

        emailSender.send(
                member.getEmail(),
                buildEmail(
                        subject(notification.getNotificationType()),
                        member.getName(),
                        notification.getMessage()
                ),
                subject(notification.getNotificationType()),
                notification
        );
    }

    private String subject(NotificationType notificationType) {
        return switch (notificationType) {
            case ACCOUNT_CREATED -> "Welcome to LibraryMan! \uD83C\uDF89";
            case ACCOUNT_DELETED -> "Account Deletion Confirmation 🗑️";
            case BORROW -> "Book Borrowed Successfully \uD83C\uDF89";
            case REMINDER -> "Reminder: Due date approaching ⏰";
            case PAID -> "Payment Received for Fine \uD83D\uDCB8";
            case FINE -> "Overdue Fine Imposed ‼️";
            case UPDATE -> "Account Details Updated \uD83D\uDD04";
            case RETURNED -> "Book Returned Successfully \uD83D\uDCDA";
        };
    }




    private String buildEmail(String notificationType,String memberName, String notificationMessage) {

        return "<div style=\"font-family:Helvetica,Arial,sans-serif; font-size:16px; margin:0; color:#0b0c0c; background-color:#ffffff\">\n" +
                "\n" +
                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                "\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
                "        \n" +
                "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "          <tbody><tr>\n" +
                "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
                "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td style=\"padding-left:10px\">\n" +
                "                  \n" +
                "                    </td>\n" +
                "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
                "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">" +notificationType+
                "</span>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "              </a>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "        </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
                "      <td>\n" +
                "        \n" +
                "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "\n" +
                "\n" +
                "\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
                "        \n" +
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + memberName + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">" + notificationMessage + "</p>" +
                "<p>Best regards,</p>" +
                "<p>LibraryMan</p>" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
                "\n" +
                "</div></div>";
    }

}

