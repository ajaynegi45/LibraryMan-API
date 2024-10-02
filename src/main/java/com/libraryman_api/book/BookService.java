package com.libraryman_api.book;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.libraryman_api.exception.ResourceNotFoundException;

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
     * Retrieves a paginated list of all books from the database.
     *
     * @param pageable the pagination information, including the page number and size
     * @return a {@link Page} of {@link Book} representing all books
     */
    public Page<Book> getAllBooks(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }

    /**
     * Retrieves a book by its ID.
     *
     * @param bookId the ID of the book to retrieve
     * @return an {@code Optional} containing the found book, or {@code Optional.empty()} if no book was found
     */
    public Optional<Book> getBookById(int bookId) {
        return bookRepository.findById(bookId);
    }

    /**
     * Adds a new book to the database.
     *
     * @param book the book to be added
     * @return the saved book
     */
    public Book addBook(Book book) {
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
    public Book updateBook(int bookId, Book bookDetails) {
        Book book = bookRepository.findById(bookId)
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
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));
        bookRepository.delete(book);
    }

}
