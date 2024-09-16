package com.libraryman_api.book;

import com.libraryman_api.exception.ResourceNotFoundException;
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
     * @return a list of {@link Book} objects representing all the books in the library.
     */
    @GetMapping
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    /**
     * Retrieves a book by its ID.
     *
     * @param id the ID of the book to retrieve.
     * @return a {@link ResponseEntity} containing the {@link Book} object, if found.
     * @throws ResourceNotFoundException if the book with the specified ID is not found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable int id) {
        return bookService.getBookById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));
    }

    /**
     * Adds a new book to the library.
     *
     * @param book the {@link Book} object representing the new book to add.
     * @return the added {@link Book} object.
     */
    @PostMapping
    public Book addBook(@RequestBody Book book) {
        return bookService.addBook(book);
    }

    /**
     * Updates an existing book in the library.
     *
     * @param id          the ID of the book to update.
     * @param bookDetails the {@link Book} object containing the updated book details.
     * @return the updated {@link Book} object.
     */
    @PutMapping("/{id}")
    public Book updateBook(@PathVariable int id, @RequestBody Book bookDetails) {
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
