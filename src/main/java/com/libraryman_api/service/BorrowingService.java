package com.libraryman_api.service;

import com.libraryman_api.entity.Books;
import com.libraryman_api.entity.Borrowings;
import com.libraryman_api.entity.Fines;
import com.libraryman_api.exception.ResourceNotFoundException;
import com.libraryman_api.repository.BorrowingRepository;
import com.libraryman_api.repository.FineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class BorrowingService {
    @Autowired
    private BorrowingRepository borrowingRepository;

    @Autowired
    private FineRepository fineRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private BookService bookService;

    public List<Borrowings> getAllBorrowings() {
        return borrowingRepository.findAll();
    }


    public Optional<Borrowings> getBorrowingById(int borrowingId) {
        return borrowingRepository.findById(borrowingId);
    }



    public Borrowings borrowBook(Borrowings borrowing) {

        Optional<Books> book = bookService.getBookById(borrowing.getBook().getBookId());

        if (book.isPresent()) {

        if(book.get().getCopiesAvailable() > 0){
            book.get().setCopiesAvailable(book.get().getCopiesAvailable() - 1);
            borrowing.setBorrowDate(new Date());
            borrowing.setDueDate(calculateDueDate());
            Borrowings savedBorrowing = borrowingRepository.save(borrowing);
            notificationService.sendReminderNotification(savedBorrowing);
            return savedBorrowing;
        }
        }


        throw new ResourceNotFoundException("Not enough copies available");
    }



    public void returnBook(int borrowingId) {
        Borrowings borrowing = borrowingRepository.findById(borrowingId)
                .orElseThrow(() -> new ResourceNotFoundException("Borrowing not found"));



        Optional<Books> book = bookService.getBookById(borrowing.getBook().getBookId());
        if (book.isPresent()) {
            if(book.get().getCopiesAvailable() > 0){
                    borrowing.setReturnDate(new Date());
                book.get().setCopiesAvailable(book.get().getCopiesAvailable() + 1);
                borrowing.getBook().setCopiesAvailable(borrowing.getBook().getCopiesAvailable() + 1);
                borrowingRepository.delete(borrowing);
            }
        }



//        // Check if overdue
//        if (borrowing.getReturnDate().after(borrowing.getDueDate())) {
//            imposeFine(borrowing);
//        }
//        else if (borrowing.getFine() != null && !borrowing.getFine().isPaid()) {
//            payFine(borrowing.getFine());
//        }else {
//            Optional<Books> book = bookService.getBookById(borrowing.getBook().getBookId());
//            if (book.isPresent()) {
//                if(book.get().getCopiesAvailable() > 0){
////                    borrowing.setReturnDate(new Date());
//                    book.get().setCopiesAvailable(book.get().getCopiesAvailable() + 1);
//                    borrowing.getBook().setCopiesAvailable(borrowing.getBook().getCopiesAvailable() + 1);
//                    borrowingRepository.save(borrowing);
//                }
//            }
//
//        }
    }



    private void imposeFine(Borrowings borrowing) {
        Fines fine = new Fines();
        fine.setBorrowing(borrowing);
        fine.setAmount(calculateFineAmount(borrowing));
        fine.setPaid(false);
        fineRepository.save(fine);
        notificationService.sendFineNotification(borrowing.getMember());
    }



    private Date calculateDueDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 14); // 14 days lending period
        return calendar.getTime();
    }




    private BigDecimal calculateFineAmount(Borrowings borrowing) {
        long overdueDays = ChronoUnit.DAYS.between(
                borrowing.getDueDate().toInstant(),
                borrowing.getReturnDate().toInstant());
        return BigDecimal.valueOf(overdueDays * 5); // Assuming a fine of 5 per day
    }



    public void payFine(Fines fine) {
        fine.setPaid(true);
        fineRepository.save(fine);
    }
}

