package com.libraryman_api.controller;

import com.libraryman_api.entity.Borrowings;
import com.libraryman_api.service.BorrowingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/borrowings")
public class BorrowingController {

    private final BorrowingService borrowingService;
    public BorrowingController(BorrowingService borrowingService) {
        this.borrowingService = borrowingService;
    }

    @GetMapping
    public List<Borrowings> getAllBorrowings() {
        return borrowingService.getAllBorrowings();
    }

    @PostMapping
    public Borrowings borrowBook(@RequestBody Borrowings borrowing) {
        return borrowingService.borrowBook(borrowing);
    }

    @PutMapping("/{id}/return")
    public void returnBook(@PathVariable int id) {
        borrowingService.returnBook(id);
    }

    @PutMapping("/{id}/pay")
    public String payFine(@PathVariable int id) {
        System.out.println("Pay Fine Id: " + id);
        return borrowingService.payFine(id);
    }
}
