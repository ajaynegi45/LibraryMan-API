package com.libraryman_api.controller;

import com.libraryman_api.entity.Books;
import com.libraryman_api.exception.ResourceNotFoundException;
import com.libraryman_api.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {
    @Autowired
    private BookService bookService;

    @GetMapping
    public List<Books> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Books> getBookById(@PathVariable int id) {
        return bookService.getBookById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));
    }

    @PostMapping
    public Books addBook(@RequestBody Books book) {
        return bookService.addBook(book);
    }

    @PutMapping("/{id}")
    public Books updateBook(@PathVariable int id, @RequestBody Books bookDetails) {
        return bookService.updateBook(id, bookDetails);
    }

    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable int id) {
        bookService.deleteBook(id);
    }
}

