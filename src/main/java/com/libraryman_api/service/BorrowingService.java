package com.libraryman_api.service;

import com.libraryman_api.entity.Books;
import com.libraryman_api.entity.Borrowings;
import com.libraryman_api.entity.Fines;
import com.libraryman_api.entity.Members;
import com.libraryman_api.exception.ResourceNotFoundException;
import com.libraryman_api.repository.BorrowingRepository;
import com.libraryman_api.repository.FineRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class BorrowingService {

    private final BorrowingRepository borrowingRepository;
    private final FineRepository fineRepository;
    private final NotificationService notificationService;
    private final BookService bookService;

    public BorrowingService(BorrowingRepository borrowingRepository, FineRepository fineRepository, NotificationService notificationService, BookService bookService) {
        this.borrowingRepository = borrowingRepository;
        this.fineRepository = fineRepository;
        this.notificationService = notificationService;
        this.bookService = bookService;
    }

    public List<Borrowings> getAllBorrowings() {
        return borrowingRepository.findAll();
    }

    public Optional<Borrowings> getBorrowingById(int borrowingId) {
        return borrowingRepository.findById(borrowingId);
    }

    @Transactional
    public synchronized Borrowings borrowBook(Borrowings borrowing) {
        Optional<Books> book = bookService.getBookById(borrowing.getBook().getBookId());

        if (book.isPresent()) {
            Books bookEntity = book.get();

            if (bookEntity.getCopiesAvailable() > 0) {
                updateBookCopies(borrowing.getBook().getBookId(), "REMOVE", 1);
                borrowing.setBorrowDate(new Date());
                borrowing.setDueDate(calculateDueDate());
                Borrowings savedBorrowing = borrowingRepository.save(borrowing);

                notificationService.sendReminderNotification(savedBorrowing);
                return savedBorrowing;
            } else {
                throw new ResourceNotFoundException("Not enough copies available");
            }
        } else {
            throw new ResourceNotFoundException("Book not found");
        }
    }

//    @Transactional
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
                throw new ResourceNotFoundException("Due date passed. Fine imposed, pay fine first to return the book");
            } else if (!borrowing.getFine().isPaid()) {
                throw new ResourceNotFoundException("Outstanding fine, please pay before returning the book");
            }
        }

        borrowing.setReturnDate(new Date());
        updateBookCopies(borrowing.getBook().getBookId(), "ADD", 1);
        borrowingRepository.save(borrowing);
    }

    private Fines imposeFine(Borrowings borrowing) {
        Fines fine = new Fines();
//        fine.setAmount(BigDecimal.valueOf(10)); // Hard Coding fine for testing purpose
        fine.setAmount(calculateFineAmount(borrowing));
        notificationService.sendFineNotification(borrowing.getMember());
        return fineRepository.save(fine);
    }



    public String payFine(int borrowingId) {
        Borrowings borrowing = getBorrowingById(borrowingId)
                .orElseThrow(() -> new ResourceNotFoundException("Borrowing not found"));
        Fines fine = borrowing.getFine();

        if (fine != null && !fine.isPaid()) {
            fine.setPaid(true);
            fineRepository.save(fine);  // Save the updated fine
            borrowingRepository.save(borrowing);  // Save borrowing with updated fine
        } else {
            throw new ResourceNotFoundException("No outstanding fine found or fine already paid");
        }

        return "PAID";
    }


    public void updateBookCopies(int bookId, String operation, int numberOfCopies) {
        Optional<Books> book = bookService.getBookById(bookId);

        if (book.isPresent()) {
            Books bookEntity = book.get();
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

    private Date calculateDueDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 14); // Adjust lending period as needed
        return calendar.getTime();
    }

    private BigDecimal calculateFineAmount(Borrowings borrowing) {
        long overdueDays = ChronoUnit.DAYS.between(
                borrowing.getDueDate().toInstant(),
                new Date().toInstant());
        return BigDecimal.valueOf(overdueDays * 10); // 10 currency units per day
    }
}
