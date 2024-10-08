package com.libraryman_api.borrowing;

import com.libraryman_api.exception.ResourceNotFoundException;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing borrowings in the LibraryMan application.
 * This controller provides endpoints for performing operations related to borrowing and returning books,
 * paying fines, and retrieving borrowing records.
 */
@RestController
@RequestMapping("/api/borrowings")
public class BorrowingController {

    private final BorrowingService borrowingService;

    /**
     * Constructs a new {@code BorrowingController} with the specified {@link BorrowingService}.
     *
     * @param borrowingService the service used to handle borrowing-related operations.
     */
    public BorrowingController(BorrowingService borrowingService) {
        this.borrowingService = borrowingService;
    }

    /**
     * Retrieves a list of all borrowing records in the library.
     *
     * @return a list of {@link Borrowings} objects representing all borrowings.
     */
    @GetMapping
    @PreAuthorize("hasRole('LIBRARIAN') or hasRole('ADMIN')")
    public List<Borrowings> getAllBorrowings() {
        return borrowingService.getAllBorrowings();
    }

    /**
     * Records a new book borrowing.
     *
     * @param borrowing the {@link Borrowings} object containing borrowing details.
     * @return the saved {@link Borrowings} object representing the borrowing record.
     */
    @PostMapping
    public Borrowings borrowBook(@RequestBody Borrowings borrowing) {
        return borrowingService.borrowBook(borrowing);
    }

    /**
     * Marks a borrowed book as returned.
     *
     * @param id the ID of the borrowing record to update.
     */
    @PutMapping("/{id}/return")
    public void returnBook(@PathVariable int id) {
        borrowingService.returnBook(id);
    }

    /**
     * Pays the fine for an overdue book.
     *
     * @param id the ID of the borrowing record for which the fine is being paid.
     * @return a message indicating the payment status.
     */
    @PutMapping("/{id}/pay")
    public String payFine(@PathVariable int id) {
        System.out.println("Pay Fine Id: " + id);
        return borrowingService.payFine(id);
    }

    /**
     * Retrieves all borrowing records for a specific member.
     *
     * @param memberId the ID of the member whose borrowing records are to be retrieved.
     * @return a list of {@link Borrowings} objects representing the member's borrowings.
     */
    @GetMapping("member/{memberId}")
    @PreAuthorize("hasRole('LIBRARIAN') or hasRole('ADMIN')")
    public List<Borrowings> getAllBorrowingsOfAMember(@PathVariable int memberId) {
        return borrowingService.getAllBorrowingsOfMember(memberId);
    }

    /**
     * Retrieves a borrowing record by its ID.
     *
     * @param borrowingId the ID of the borrowing record to retrieve.
     * @return the {@link Borrowings} object representing the borrowing record.
     * @throws ResourceNotFoundException if the borrowing record with the specified ID is not found.
     */
    @GetMapping("{borrowingId}")
    @PreAuthorize("hasRole('LIBRARIAN') or hasRole('ADMIN')")
    public Borrowings getBorrowingById(@PathVariable int borrowingId) {
        return borrowingService.getBorrowingById(borrowingId)
                .orElseThrow(() -> new ResourceNotFoundException("Borrowing not found"));
    }
}
