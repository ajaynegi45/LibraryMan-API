package com.libraryman_api.book;

import com.libraryman_api.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing books in the LibraryMan application.
 *
 * <p>This controller provides a set of endpoints for performing CRUD operations 
 * on books. The operations include retrieving a list of all books, fetching 
 * a book by its ID, adding a new book, updating an existing book, and deleting 
 * a book.</p>
 *
 * <p>All endpoints are accessible under the base URL path <code>/api/books</code>.</p>
 * 
 * <p>Note: The {@link ResourceNotFoundException} is thrown when an invalid book ID is 
 * provided for retrieval, update, or deletion.</p>
 * 
 * <p>Usage examples for each endpoint can be found in the API documentation.</p>
 */
@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    /**
     * Constructs a new {@code BookController} with the specified {@code BookService}.
     *
     * @param bookService the service used to manage book operations
     */
    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    /**
     * Retrieves a list of all books in the library.
     *
     * <p>This endpoint handles GET requests to <code>/api/books</code> and 
     * returns a list of all books currently available in the library.</p>
     *
     * @return a list of {@link Book} objects representing all books in the library
     */
    @GetMapping
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    /**
     * Retrieves a specific book by its ID.
     *
     * <p>This endpoint handles GET requests to <code>/api/books/{id}</code> 
     * and returns the details of the book with the specified ID.</p>
     *
     * @param id the ID of the book to retrieve
     * @return a {@link ResponseEntity} containing the {@link Book} object if found
     * @throws ResourceNotFoundException if the book with the specified ID is not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable int id) {
        return bookService.getBookById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Book with ID " + id + " not found"));
    }

    /**
     * Adds a new book to the library.
     *
     * <p>This endpoint handles POST requests to <code>/api/books</code> 
     * to add a new book to the library.</p>
     *
     * @param book the {@link Book} object representing the new book to add
     * @return the added {@link Book} object
     */
    @PostMapping
    public Book addBook(@RequestBody Book book) {
        return bookService.addBook(book);
    }

    /**
     * Updates an existing book in the library.
     *
     * <p>This endpoint handles PUT requests to <code>/api/books/{id}</code> 
     * to update the details of an existing book in the library.</p>
     *
     * @param id the ID of the book to update
     * @param bookDetails the updated {@link Book} details
     * @return the updated {@link Book} object
     * @throws ResourceNotFoundException if the book with the specified ID is not found
     */
    @PutMapping("/{id}")
    public Book updateBook(@PathVariable int id, @RequestBody Book bookDetails) {
        return bookService.updateBook(id, bookDetails);
    }

    /**
     * Deletes a book from the library by its ID.
     *
     * <p>This endpoint handles DELETE requests to <code>/api/books/{id}</code> 
     * to remove a book from the library.</p>
     *
     * @param id the ID of the book to delete
     * @throws ResourceNotFoundException if the book with the specified ID is not found
     */
    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable int id) {
        bookService.deleteBook(id);
    }
}
