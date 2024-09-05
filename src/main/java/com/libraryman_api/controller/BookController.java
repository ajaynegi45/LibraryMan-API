package com.libraryman_api.controller;

import com.libraryman_api.entity.Books;
import com.libraryman_api.exception.ResourceNotFoundException;
import com.libraryman_api.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing books in the LibraryMan application.
 * This controller provides endpoints for performing CRUD operations on books,
 * including retrieving all books, getting a book by its ID, adding a new book,
 * updating an existing book, and deleting a book.
 */
@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookService bookService;

    /**
     * Retrieves a list of all books in the library.
     *
     * @return a list of {@link Books} objects representing all the books in the library.
     */
    @GetMapping
    public List<Books> getAllBooks() {
        return bookService.getAllBooks();
    }

    /**
     * Retrieves a book by its ID.
     *
     * @param id the ID of the book to retrieve.
     * @return a {@link ResponseEntity} containing the {@link Books} object, if found.
     * @throws ResourceNotFoundException if the book with the specified ID is not found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Books> getBookById(@PathVariable int id) {
        return bookService.getBookById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));
    }

    /**
     * Adds a new book to the library.
     *
     * @param book the {@link Books} object representing the new book to add.
     * @return the added {@link Books} object.
     */
    @PostMapping
    public Books addBook(@RequestBody Books book) {
        return bookService.addBook(book);
    }

    /**
     * Updates an existing book in the library.
     *
     * @param id          the ID of the book to update.
     * @param bookDetails the {@link Books} object containing the updated book details.
     * @return the updated {@link Books} object.
     */
    @PutMapping("/{id}")
    public Books updateBook(@PathVariable int id, @RequestBody Books bookDetails) {
        return bookService.updateBook(id, bookDetails);
    }

    /**
     * Deletes a book from the library by its ID.
     *
     * @param id the ID of the book to delete.
     */
    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable int id) {
        bookService.deleteBook(id);
    }
}
