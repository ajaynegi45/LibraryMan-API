package com.libraryman_api.borrowing;

import com.libraryman_api.book.BookDto;
import com.libraryman_api.book.BookService;
import com.libraryman_api.book.Book;
import com.libraryman_api.fine.Fines;
import com.libraryman_api.exception.InvalidSortFieldException;
import com.libraryman_api.exception.ResourceNotFoundException;
import com.libraryman_api.fine.FineRepository;
import com.libraryman_api.member.MemberService;
import com.libraryman_api.member.Members;
import com.libraryman_api.member.MembersDto;
import com.libraryman_api.notification.NotificationService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
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
     * Retrieves a paginated list of all borrowing records from the database.
     *
     * @param pageable the pagination information, including the page number and size
     * @return a {@link Page} of {@link Borrowings} representing all borrowings
     * @throws InvalidSortFieldException if an invalid sortBy field is specified
     */
    public Page<BorrowingsDto> getAllBorrowings(Pageable pageable) {
        try {
            Page<Borrowings> pagedBorrowings = borrowingRepository.findAll(pageable);
            return pagedBorrowings.map(this::EntityToDto);
        } catch (PropertyReferenceException ex) {
            throw new InvalidSortFieldException("The specified 'sortBy' value is invalid.");
        }
    }

    /**
     * Retrieves a borrowing record by its ID.
     *
     * @param borrowingId the ID of the borrowing to retrieve
     * @return an {@code Optional} containing the found borrowing, or {@code Optional.empty()} if no borrowing was found
     */
    public Optional<BorrowingsDto> getBorrowingById(int borrowingId) {
        Optional<Borrowings> borrowingsById = borrowingRepository.findById(borrowingId);
        return borrowingsById.map(this::EntityToDto);
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
    public synchronized BorrowingsDto borrowBook(BorrowingsDto borrowing) {
        Optional<BookDto> bookDto = bookService.getBookById(borrowing.getBook().getBookId());
        Optional<MembersDto> memberDto = memberService.getMemberById(borrowing.getMember().getMemberId());
        if (bookDto.isPresent() && memberDto.isPresent()) {
            Book bookEntity = bookService.DtoToEntity(bookDto.get());
            Members memberEntity = memberService.DtoEntity(memberDto.get());

            if (bookEntity.getCopiesAvailable() > 0) {
                updateBookCopies(bookDto, "REMOVE", 1);
                borrowing.setBorrowDate(new Date());
                borrowing.setBook(bookService.EntityToDto(bookEntity));
                borrowing.setMember(memberService.EntityToDto(memberEntity));
                borrowing.setDueDate(calculateDueDate());

                Borrowings savedBorrowing = borrowingRepository.save(DtoToEntity(borrowing));

                notificationService.borrowBookNotification(savedBorrowing); // Null Book problem
                notificationService.reminderNotification(savedBorrowing); // send this notification two days before the due date // Null Book problem
                return EntityToDto(savedBorrowing);
            } else {
                throw new ResourceNotFoundException("Not enough copies available");
            }
        } else {
            if (bookDto.isEmpty()) {
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
    public synchronized BorrowingsDto returnBook(int borrowingId) {
        BorrowingsDto borrowingsDto = getBorrowingById(borrowingId)
                .orElseThrow(() -> new ResourceNotFoundException("Borrowing not found"));
        Optional<MembersDto> memberDto = memberService.getMemberById(borrowingsDto.getMember().getMemberId());
        if(!memberDto.isPresent()){
                throw new ResourceNotFoundException("Member not found");
        }
        if (borrowingsDto.getReturnDate() != null) {
            throw new ResourceNotFoundException("Book has already been returned");
        }
        if (borrowingsDto.getDueDate().before(new Date())) {
            if (borrowingsDto.getFine() == null) {
                borrowingsDto.setFine(imposeFine(DtoToEntity(borrowingsDto)));
                borrowingRepository.save(DtoToEntity(borrowingsDto));
                notificationService.fineImposedNotification(DtoToEntity(borrowingsDto));
                throw new ResourceNotFoundException("Due date passed. Fine imposed, pay fine first to return the book");
            } else if (!borrowingsDto.getFine().isPaid()) {
                notificationService.fineImposedNotification(DtoToEntity(borrowingsDto));
                throw new ResourceNotFoundException("Outstanding fine, please pay before returning the book");
            }
        }

        borrowingsDto.setReturnDate(new Date());
        Optional<BookDto> bookDto = bookService.getBookById(borrowingsDto.getBook().getBookId());
        updateBookCopies(bookDto, "ADD", 1);
        notificationService.bookReturnedNotification(DtoToEntity(borrowingsDto));
        borrowingRepository.save(DtoToEntity(borrowingsDto));
        return borrowingsDto;
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
        BorrowingsDto borrowingsDto = getBorrowingById(borrowingId)
                .orElseThrow(() -> new ResourceNotFoundException("Borrowing not found"));
        Optional<MembersDto> memberDto = memberService.getMemberById(borrowingsDto.getMember().getMemberId());
        if(!memberDto.isPresent()){
            throw new ResourceNotFoundException("Member not found");
        }
        Fines fine = borrowingsDto.getFine();

        if (fine != null && !fine.isPaid()) {
            fine.setPaid(true);
            notificationService.finePaidNotification(DtoToEntity(borrowingsDto));
            fineRepository.save(fine);  // Save the updated fine
            borrowingRepository.save(DtoToEntity(borrowingsDto));  // Save borrowing with updated fine
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
    public void updateBookCopies(Optional<BookDto> bookDto, String operation, int numberOfCopies) {
        if (bookDto.isPresent()) {
            Book bookEntity = bookService.DtoToEntity(bookDto.get());
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
     * Retrieves a paginated list of all borrowing associated with a specific member.
     *
     * @param memberId the ID of the member whose borrowings are to be retrieved
     * @param pageable the pagination information, including the page number and size
     * @throws ResourceNotFoundException if the member has not borrowed any books
     * @throws InvalidSortFieldException if an invalid sortBy field is specified
     * @return a {@link Page} of {@link Borrowings} representing all borrowing associated with a specific member
     */
    public Page<BorrowingsDto> getAllBorrowingsOfMember(int memberId, Pageable pageable) {
        try {
            Page<Borrowings> borrowings = borrowingRepository.findByMember_memberId(memberId, pageable);
            
            if (borrowings.isEmpty()) {
                throw new ResourceNotFoundException("Member didn't borrow any book");
            }
            return borrowings.map(this::EntityToDto);
        } catch (PropertyReferenceException ex) {
            throw new InvalidSortFieldException("The specified 'sortBy' value is invalid.");
        }
    }
    /**
     * Converts a BorrowingsDto object to a Borrowings entity.
     *
     * <p>This method takes a BorrowingsDto object and transforms it into a Borrowings
     * entity for use in database operations. It maps all relevant borrowing details
     * from the DTO, including borrow date, member information, fine, return date,
     * due date, and book details. It also retrieves and converts related entities
     * such as Member and Book using respective service methods.</p>
     *
     * @param borrowingsDto the DTO object containing borrowing information
     * @return a Borrowings entity with data populated from the DTO
     */


    public Borrowings DtoToEntity(BorrowingsDto borrowingsDto){
        Borrowings borrowings = new Borrowings();
        borrowings.setBorrowDate(borrowingsDto.getBorrowDate());
        borrowings.setMember(memberService.DtoEntity(borrowingsDto.getMember()));
        borrowings.setFine(borrowingsDto.getFine());
        borrowings.setReturnDate(borrowingsDto.getReturnDate());
        borrowings.setDueDate(borrowingsDto.getDueDate());
        borrowings.setBook(bookService.DtoToEntity(borrowingsDto.getBook()));
        borrowings.setBorrowingId(borrowingsDto.getBorrowingId());
        return borrowings;
    }
    /**
     * Converts a Borrowings entity to a BorrowingsDto object.
     *
     * <p>This method takes a Borrowings entity and converts it into a BorrowingsDto
     * object for data transfer between application layers. It maps all necessary
     * borrowing details from the entity, including borrowing ID, fine, borrow date,
     * return date, due date, and related Member and Book entities, converting them
     * into DTOs using respective service methods.</p>
     *
     * @param borrowings the entity object containing borrowing information
     * @return a BorrowingsDto object with data populated from the entity
     */


    public BorrowingsDto EntityToDto(Borrowings borrowings){
        BorrowingsDto borrowingsDto = new BorrowingsDto();
        borrowingsDto.setBorrowingId(borrowings.getBorrowingId());
        borrowingsDto.setFine(borrowings.getFine());
        borrowingsDto.setBorrowDate(borrowings.getBorrowDate());
        borrowingsDto.setReturnDate(borrowings.getReturnDate());
        borrowingsDto.setDueDate(borrowings.getDueDate());
        borrowingsDto.setMember(memberService.EntityToDto(borrowings.getMember()));
        borrowingsDto.setBook(bookService.EntityToDto(borrowings.getBook()));
        return borrowingsDto;
    }
}
