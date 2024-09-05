package com.libraryman_api.service;

import com.libraryman_api.entity.Books;
import com.libraryman_api.exception.ResourceNotFoundException;
import com.libraryman_api.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class for managing books in the LibraryMan system.
 *
 * <p>This service provides methods to perform CRUD operations on books, including
 * retrieving all books, retrieving a book by its ID, adding a new book, updating
 * an existing book, and deleting a book by its ID.</p>
 *
 * <p>Each method in this service interacts with the {@link BookRepository} to
 * perform database operations.</p>
 *
 * <p>In the case of an invalid book ID being provided, the service throws a
 * {@link ResourceNotFoundException}.</p>
 *
 * @author Ajay Negi
 */
@Service
public class BookService {

    private final BookRepository bookRepository;

    /**
     * Constructs a new {@code BookService} with the specified {@code BookRepository}.
     *
     * @param bookRepository the repository to be used by this service to interact with the database
     */
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    /**
     * Retrieves all books from the database.
     *
     * @return a list of all books
     */
    public List<Books> getAllBooks() {
        return bookRepository.findAll();
    }

    /**
     * Retrieves a book by its ID.
     *
     * @param bookId the ID of the book to retrieve
     * @return an {@code Optional} containing the found book, or {@code Optional.empty()} if no book was found
     */
    public Optional<Books> getBookById(int bookId) {
        return bookRepository.findById(bookId);
    }

    /**
     * Adds a new book to the database.
     *
     * @param book the book to be added
     * @return the saved book
     */
    public Books addBook(Books book) {
        return bookRepository.save(book);
    }

    /**
     * Updates an existing book with the given details.
     *
     * @param bookId the ID of the book to update
     * @param bookDetails the new details for the book
     * @return the updated book
     * @throws ResourceNotFoundException if the book with the specified ID is not found
     */
    public Books updateBook(int bookId, Books bookDetails) {
        Books book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));
        book.setTitle(bookDetails.getTitle());
        book.setAuthor(bookDetails.getAuthor());
        book.setIsbn(bookDetails.getIsbn());
        book.setPublisher(bookDetails.getPublisher());
        book.setPublishedYear(bookDetails.getPublishedYear());
        book.setGenre(bookDetails.getGenre());
        book.setCopiesAvailable(bookDetails.getCopiesAvailable());
        return bookRepository.save(book);
    }

    /**
     * Deletes a book by its ID.
     *
     * @param bookId the ID of the book to delete
     * @throws ResourceNotFoundException if the book with the specified ID is not found
     */
    public void deleteBook(int bookId) {
        Books book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));
        bookRepository.delete(book);
    }

}
