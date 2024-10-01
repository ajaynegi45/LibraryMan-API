package com.libraryman_api.borrowing;

import com.libraryman_api.book.BookService;
import com.libraryman_api.book.Book;
import com.libraryman_api.fine.Fines;
import com.libraryman_api.exception.ResourceNotFoundException;
import com.libraryman_api.fine.FineRepository;
import com.libraryman_api.member.MemberService;
import com.libraryman_api.member.Members;
import com.libraryman_api.notification.NotificationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Service class for managing the borrowing and returning of books in the LibraryMan system.
 *
 * <p>This service handles the borrowing and returning processes, including calculating
 * due dates, imposing fines for overdue returns, and managing the availability of books.
 * It also integrates with the {@link NotificationService} to send notifications
 * related to borrowing, returning, and fines.</p>
 *
 * <p>Each method interacts with the {@link BorrowingRepository} and {@link FineRepository}
 * to perform database operations, ensuring consistency and proper transactional behavior.</p>
 *
 * <p>In cases where a book or borrowing record is not found, or if an operation cannot be completed
 * (e.g., returning a book with an outstanding fine), the service throws a
 * {@link ResourceNotFoundException}.</p>
 *
 * @author Ajay Negi
 */
@Service
public class BorrowingService {

    private final BorrowingRepository borrowingRepository;
    private final FineRepository fineRepository;
    private final NotificationService notificationService;
    private final BookService bookService;
    private final MemberService memberService;

    /**
     * Constructs a new {@code BorrowingService} with the specified repositories and services.
     *
     * @param borrowingRepository the repository for managing borrowing records
     * @param fineRepository the repository for managing fine records
     * @param notificationService the service for sending notifications
     * @param bookService the service for managing book records
     */
    public BorrowingService(BorrowingRepository borrowingRepository, FineRepository fineRepository, NotificationService notificationService, BookService bookService, MemberService memberService) {
        this.borrowingRepository = borrowingRepository;
        this.fineRepository = fineRepository;
        this.notificationService = notificationService;
        this.bookService = bookService;
        this.memberService=memberService;
    }

    /**
     * Retrieves all borrowings from the database.
     *
     * @return a list of all borrowings
     */
    public List<Borrowings> getAllBorrowings() {
        return borrowingRepository.findAll();
    }

    /**
     * Retrieves a borrowing record by its ID.
     *
     * @param borrowingId the ID of the borrowing to retrieve
     * @return an {@code Optional} containing the found borrowing, or {@code Optional.empty()} if no borrowing was found
     */
    public Optional<Borrowings> getBorrowingById(int borrowingId) {
        return borrowingRepository.findById(borrowingId);
    }

    /**
     * Manages the borrowing process for a book.
     *
     * <p>This method is synchronized to ensure thread safety and is marked as
     * {@link Transactional} to ensure that all database operations complete successfully
     * or roll back in case of any errors. It updates the book's availability, sets the
     * borrowing and due dates, and sends notifications related to the borrowing.</p>
     *
     * @param borrowing the borrowing details provided by the user
     * @return the saved borrowing record
     * @throws ResourceNotFoundException if the book is not found or if there are not enough copies available
     */
    @Transactional
    public synchronized Borrowings borrowBook(Borrowings borrowing) {
        Optional<Book> book = bookService.getBookById(borrowing.getBook().getBookId());
        Optional<Members> member = memberService.getMemberById(borrowing.getMember().getMemberId());
        if (book.isPresent() && member.isPresent()) {
            Book bookEntity = book.get();
            Members memberEntity = member.get();

            if (bookEntity.getCopiesAvailable() > 0) {
                updateBookCopies(borrowing.getBook().getBookId(), "REMOVE", 1);
                borrowing.setBorrowDate(new Date());
                borrowing.setBook(bookEntity);
                borrowing.setMember(memberEntity);
                borrowing.setDueDate(calculateDueDate());

                Borrowings savedBorrowing = borrowingRepository.save(borrowing);

                notificationService.borrowBookNotification(savedBorrowing); // Null Book problem
                notificationService.reminderNotification(savedBorrowing); // send this notification two days before the due date // Null Book problem
                return savedBorrowing;
            } else {
                throw new ResourceNotFoundException("Not enough copies available");
            }
        } else {
            if (book.isEmpty()) {
                throw new ResourceNotFoundException("Book not found");
            }
            throw new ResourceNotFoundException("Member not found");
        }
    }

    /**
     * Manages the return process for a borrowed book.
     *
     * <p>This method is synchronized to ensure thread safety. It checks for overdue returns,
     * imposes fines if necessary, and updates the book's availability. Notifications are sent
     * for fines and successful returns. If a fine is imposed, the book cannot be returned until
     * the fine is paid.</p>
     *
     * @param borrowingId the ID of the borrowing record
     * @throws ResourceNotFoundException if the borrowing record is not found, if the book has already been returned, or if there are outstanding fines
     */
    public synchronized void returnBook(int borrowingId) {
        Borrowings borrowing = getBorrowingById(borrowingId)
                .orElseThrow(() -> new ResourceNotFoundException("Borrowing not found"));

        if (borrowing.getReturnDate() != null) {
            throw new ResourceNotFoundException("Book has already been returned");
        }
        if (borrowing.getDueDate().before(new Date())) {
            if (borrowing.getFine() == null) {
                borrowing.setFine(imposeFine(borrowing));
                borrowingRepository.save(borrowing);
                notificationService.fineImposedNotification(borrowing);
                throw new ResourceNotFoundException("Due date passed. Fine imposed, pay fine first to return the book");
            } else if (!borrowing.getFine().isPaid()) {
                notificationService.fineImposedNotification(borrowing);
                throw new ResourceNotFoundException("Outstanding fine, please pay before returning the book");
            }
        }

        borrowing.setReturnDate(new Date());
        updateBookCopies(borrowing.getBook().getBookId(), "ADD", 1);
        notificationService.bookReturnedNotification(borrowing);
        borrowingRepository.save(borrowing);
    }

    /**
     * Imposes a fine on a borrowing for overdue returns.
     *
     * @param borrowing the borrowing record with an overdue return
     * @return the saved fine record
     */
    private Fines imposeFine(Borrowings borrowing) {
        Fines fine = new Fines();
        fine.setAmount(calculateFineAmount(borrowing));
        return fineRepository.save(fine);
    }

    /**
     * Processes the payment of a fine associated with a borrowing.
     *
     * <p>If the fine is successfully paid, a notification is sent. The borrowing and fine
     * records are updated in the database.</p>
     *
     * @param borrowingId the ID of the borrowing record with the fine
     * @return a confirmation string indicating the fine has been paid
     * @throws ResourceNotFoundException if the borrowing record is not found or if there is no outstanding fine
     */
    public String payFine(int borrowingId) {
        Borrowings borrowing = getBorrowingById(borrowingId)
                .orElseThrow(() -> new ResourceNotFoundException("Borrowing not found"));
        Fines fine = borrowing.getFine();

        if (fine != null && !fine.isPaid()) {
            fine.setPaid(true);
            notificationService.finePaidNotification(borrowing);
            fineRepository.save(fine);  // Save the updated fine
            borrowingRepository.save(borrowing);  // Save borrowing with updated fine
        } else {
            throw new ResourceNotFoundException("No outstanding fine found or fine already paid");
        }
        return "PAID";
    }

    /**
     * Updates the number of available copies of a book in the library.
     *
     * <p>This method increases or decreases the number of copies based on the specified operation.
     * If there are not enough copies available for a removal operation, or if the book is not found,
     * a {@link ResourceNotFoundException} is thrown.</p>
     *
     * @param bookId the ID of the book to update
     * @param operation the operation to perform ("ADD" to increase, "REMOVE" to decrease)
     * @param numberOfCopies the number of copies to add or remove
     * @throws ResourceNotFoundException if the book is not found or if there are not enough copies to remove
     */
    public void updateBookCopies(int bookId, String operation, int numberOfCopies) {
        Optional<Book> book = bookService.getBookById(bookId);

        if (book.isPresent()) {
            Book bookEntity = book.get();
            if (operation.equals("ADD")) {
                bookEntity.setCopiesAvailable(bookEntity.getCopiesAvailable() + numberOfCopies);
            } else if (operation.equals("REMOVE")) {
                if (bookEntity.getCopiesAvailable() >= numberOfCopies) {
                    bookEntity.setCopiesAvailable(bookEntity.getCopiesAvailable() - numberOfCopies);
                } else {
                    throw new ResourceNotFoundException("Not enough copies are available");
                }
            }
        } else {
            throw new ResourceNotFoundException("Book not found");
        }
    }

    /**
     * Calculates the due date for a borrowing.
     *
     * @return the calculated due date, which is 15 days from the current date
     */
    private Date calculateDueDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 15);
        return calendar.getTime();
    }

    /**
     * Calculates the fine amount for an overdue borrowing.
     *
     * <p>The fine is calculated based on the number of overdue days multiplied by a fixed
     * rate of 10 rupees per day.</p>
     *
     * @param borrowing the borrowing record for which the fine is being calculated
     * @return the calculated fine amount
     */
    private BigDecimal calculateFineAmount(Borrowings borrowing) {
        long overdueDays = ChronoUnit.DAYS.between(
                borrowing.getDueDate().toInstant(),
                new Date().toInstant());
        System.out.println("Over Due Days" + overdueDays);
        System.out.println("Fine Amount" + (overdueDays * 10));
        return BigDecimal.valueOf(overdueDays * 10); // 10 rupees per day fine
    }

    /**
     * Retrieves all borrowings associated with a specific member.
     *
     * @param memberId the ID of the member whose borrowings are to be retrieved
     * @return a list of borrowings associated with the specified member
     * @throws ResourceNotFoundException if the member has not borrowed any books
     */
    public List<Borrowings> getAllBorrowingsOfMember(int memberId) {
        return borrowingRepository.findByMember_memberId(memberId).orElseThrow(() -> new ResourceNotFoundException("Member didn't borrow any book"));
    }
}
