package com.libraryman_api.notification;

import com.libraryman_api.borrowing.Borrowings;
import com.libraryman_api.borrowing.BorrowingsDto;
import com.libraryman_api.email.EmailSender;
import com.libraryman_api.exception.ResourceNotFoundException;
import com.libraryman_api.member.MemberRepository;
import com.libraryman_api.member.Members;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;


/**
 * Service class responsible for managing notifications within the LibraryMan application.
 * This service handles various notification-related tasks, such as sending email notifications
 * for account creation, deletion, borrowing books, returning books, fines, and account updates.
 * It interacts with the {@link NotificationRepository}, {@link MemberRepository}, and {@link EmailSender}
 * to perform these tasks.
 */
@Service
public class NotificationService {
    private final EmailSender emailSender;
    private final NotificationRepository notificationRepository;
    private final MemberRepository memberRepository;
    @Autowired
    private ModelMapper mapper;

    /**
     * Constructs a new {@code NotificationService} with the specified {@link EmailSender},
     * {@link NotificationRepository}, and {@link MemberRepository}.
     *
     * @param emailSender            the service responsible for sending emails.
     * @param notificationRepository the repository to manage notifications in the database.
     * @param memberRepository       the repository to manage members in the database.
     */
    public NotificationService(EmailSender emailSender, NotificationRepository notificationRepository, MemberRepository memberRepository) {
        this.emailSender = emailSender;
        this.notificationRepository = notificationRepository;
        this.memberRepository = memberRepository;
    }

    /**
     * Sends a notification to a member when their account is created.
     *
     * @param members the member whose account has been created.
     */
    public void accountCreatedNotification(Members members) {
        Notifications notification = new Notifications();
        notification.setMember(members);
        notification.setMessage("We’re excited to welcome you to LibraryMan! Your account has been successfully created, and you’re now part of our community of book lovers. 📚<br><br>Feel free to explore our vast collection of books and other resources. If you have any questions or need assistance, our team is here to help.<br><br>Happy reading! 📖");
        notification.setNotificationType(NotificationType.ACCOUNT_CREATED);
        notification.setSentDate(new Timestamp(System.currentTimeMillis()));

        sendNotification(notification);
        notificationRepository.save(notification);
    }

    /**
     * Sends a notification to a member when their account is deleted.
     *
     * @param members the member whose account has been deleted.
     */
    public void accountDeletionNotification(Members members) {
        Notifications notification = new Notifications();
        notification.setMember(members);
        notification.setMessage("We’re sorry to see you go! Your account with LibraryMan has been successfully deleted as per your request.<br><br>If you change your mind in the future, you’re always welcome to create a new account with us. Should you have any questions or concerns, please don’t hesitate to reach out.<br><br>Thank you for being a part of our community.");
        notification.setNotificationType(NotificationType.ACCOUNT_DELETED);
        notification.setSentDate(new Timestamp(System.currentTimeMillis()));
        sendNotification(notification);
    }

    /**
     * Sends a notification to a member when they borrow a book.
     *
     * @param borrowing the borrowing instance containing information about the borrowed book.
     */
    public void borrowBookNotification(Borrowings borrowing) {
        Notifications notification = new Notifications();
        notification.setMember(borrowing.getMember());
        notification.setMessage("Congratulations! 🎉 You have successfully borrowed '" +
                borrowing.getBook().getTitle() + "' on " +
                LocalDateTime.ofInstant(borrowing.getBorrowDate().toInstant(), ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("dd MMMM yyyy"))

                +
                ".<br><br>You now have 15 days to enjoy reading it. We kindly request that you return it to us on or before " +
                LocalDateTime.ofInstant(borrowing.getDueDate().toInstant(), ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("dd MMMM yyyy"))
                +
                " to avoid any late fees 📆, which are ₹10 per day for late returns.<br><br>If you need to renew the book or have any questions, please don't hesitate to reach out to us.<br><br>Thank you for choosing our library!");
        notification.setNotificationType(NotificationType.BORROW);
        notification.setSentDate(new Timestamp(System.currentTimeMillis()));

        sendNotification(notification);
        notificationRepository.save(notification);
    }

    /**
     * Sends a reminder notification to a member when the due date for returning a borrowed book is approaching.
     *
     * @param borrowing the borrowing instance containing information about the borrowed book.
     */
    public void reminderNotification(Borrowings borrowing) {
        Notifications notification = new Notifications();
        notification.setMember(borrowing.getMember());
        notification.setMessage("This is a friendly reminder that the due date to return '" +
                borrowing.getBook().getTitle() + "' is approaching. Please ensure that you return the book by " +

                LocalDateTime.ofInstant(borrowing.getDueDate().toInstant(), ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("dd MMMM yyyy HH:mm")) +
                " to avoid any late fees. 📅" +
                "<br><br>If you need more time, consider renewing your book through our online portal or by contacting us." +
                "<br><br>Thank you, and happy reading! 😊");
        notification.setNotificationType(NotificationType.REMINDER);
        notification.setSentDate(new Timestamp(System.currentTimeMillis()));

        sendNotification(notification);
        notificationRepository.save(notification);
    }

    /**
     * Sends a notification to a member when they pay a fine for an overdue book.
     *
     * @param borrowing the borrowing instance containing information about the overdue book and the fine paid.
     */
    public void finePaidNotification(Borrowings borrowing) {
        Notifications notification = new Notifications();
        notification.setMember(borrowing.getMember());
        notification.setMessage("Thank you for your payment. We’ve received your payment of ₹" +
                borrowing.getFine().getAmount() + " towards the fine for the overdue return of '" + borrowing.getBook().getTitle() + "'. ✅" +
                "<br><br>Your account has been updated accordingly. If you have any questions or need further assistance, please feel free to reach out.<br><br>Thank you for your prompt payment.");
        notification.setNotificationType(NotificationType.PAID);
        notification.setSentDate(new Timestamp(System.currentTimeMillis()));

        sendNotification(notification);
        notificationRepository.save(notification);
    }

    /**
     * Sends a notification to a member when a fine is imposed for the late return of a borrowed book.
     *
     * @param borrowingsDto the borrowing instance containing information about the overdue book and the fine imposed.
     */
    public void fineImposedNotification(BorrowingsDto borrowingsDto) {
        Notifications notification = new Notifications();
        notification.setMember(mapper.map(borrowingsDto.getMember(),Members.class));
        notification.setMessage("We hope you enjoyed reading '" +
                borrowingsDto.getBook().getTitle() +
                "'. Unfortunately, our records show that the book was returned after the due date of " +
                LocalDateTime.ofInstant(borrowingsDto.getDueDate().toInstant(), ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("dd MMMM yyyy HH:mm")) +
                ". As a result, a fine of ₹10 per day has been imposed for the late return.<br><br>The total fine amount for this overdue return is ₹" +
                borrowingsDto.getFine().getAmount() +
                ".<br><br>If you have any questions or would like to discuss this matter further, please don't hesitate to contact us.<br><br>Thank you for your understanding and for being a valued member of our library.");
        notification.setNotificationType(NotificationType.FINE);
        notification.setSentDate(new Timestamp(System.currentTimeMillis()));

        sendNotification(notification);
        notificationRepository.save(notification);
    }

    /**
     * Sends a notification to a member when their account details are updated.
     *
     * @param members the member whose account details have been updated.
     */
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

    /**
     * Sends a notification to a member when they return a borrowed book.
     *
     * @param borrowing the borrowing instance containing information about the returned book.
     */
    public void bookReturnedNotification(Borrowings borrowing) {
        Notifications notification = new Notifications();
        notification.setMember(borrowing.getMember());
        notification.setMessage("Thank you for returning '" + borrowing.getBook().getTitle() + "' book on " + LocalDateTime.ofInstant(borrowing.getReturnDate().toInstant(), ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("dd MMMM yyyy HH:mm")) + ". We hope you enjoyed the book!" +
                "<br><br>Feel free to explore our collection for your next read. If you have any questions or need assistance, we’re here to help." +
                "<br><br>Thank you for choosing LibraryMan!");
        notification.setNotificationType(NotificationType.RETURNED);
        notification.setSentDate(new Timestamp(System.currentTimeMillis()));

        sendNotification(notification);
        notificationRepository.save(notification);
    }

    /**
     * Sends the notification email to the member.
     *
     * @param notification the notification instance containing information about the notification.
     */
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

    /**
     * Builds the email content based on the notification type, member name, and notification message.
     *
     * @param notificationType    the type of notification.
     * @param memberName          the name of the member.
     * @param notificationMessage the notification message to include in the email.
     * @return the built email content.
     */
    private String buildEmail(NotificationType notificationType, String memberName, String notificationMessage) {
        return "Hello " + memberName + ",<br><br>" + notificationMessage + "<br><br>" + "Best regards,<br>The LibraryMan Team";
    }

    /**
     * Determines the subject line for the email based on the notification type.
     *
     * @param notificationType the type of notification.
     * @return the subject line for the email.
     */
    private String subject(NotificationType notificationType) {
        switch (notificationType) {
            case ACCOUNT_CREATED:
                return "Welcome to LibraryMan!";
            case ACCOUNT_DELETED:
                return "Your LibraryMan Account has been Deleted";
            case BORROW:
                return "Book Borrowed Successfully";
            case REMINDER:
                return "Reminder: Book Due Date Approaching";
            case PAID:
                return "Payment Received";
            case FINE:
                return "Fine Imposed for Late Return";
            case RETURNED:
                return "Thank You for Returning Your Book";
            case UPDATE:
                return "Your Account Details Have Been Updated";
            default:
                return "LibraryMan Notification";
        }
    }


    private String buildEmail(String notificationType, String memberName, String notificationMessage) {

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
                "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">" + notificationType +
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

