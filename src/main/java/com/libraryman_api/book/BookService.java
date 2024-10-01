package com.libraryman_api.book;

import com.libraryman_api.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class responsible for managing book-related operations in the LibraryMan system.
 *
 * <p>This service handles various CRUD operations, such as retrieving all books, fetching 
 * a book by its ID, adding a new book, updating book details, and deleting a book from the system.</p>
 *
 * <p>All interactions with the database are facilitated via the {@link BookRepository}. 
 * If a book with a given ID is not found, the service throws a 
 * {@link ResourceNotFoundException}.</p>
 * 
 * <p>Usage examples and further documentation on the book management process 
 * can be found in the associated API documentation.</p>
 * 
 * @see BookRepository
 * @see ResourceNotFoundException
 * @author
 */
@Service
public class BookService {

    private final BookRepository bookRepository;

    /**
     * Constructs a new {@code BookService} instance, injecting the required 
     * {@link BookRepository} to facilitate database operations.
     *
     * @param bookRepository the repository used to access and modify book data in the database
     */
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    /**
     * Retrieves all books stored in the library.
     *
     * <p>This method interacts with the database to fetch a complete list of 
     * all {@link Book} entities available in the library's collection.</p>
     *
     * @return a {@link List} of all books in the system
     */
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    /**
     * Fetches a specific book by its ID.
     *
     * <p>If the book with the specified ID exists, it is returned as an 
     * {@link Optional}. If the book is not found, {@code Optional.empty()} is returned.</p>
     *
     * @param bookId the unique identifier of the book to be retrieved
     * @return an {@link Optional} containing the book if found, or {@code Optional.empty()} if not
     */
    public Optional<Book> getBookById(int bookId) {
        return bookRepository.findById(bookId);
    }

    /**
     * Adds a new book to the library.
     *
     * <p>This method saves the provided {@link Book} entity to the database. 
     * The book entity must contain the necessary details such as title, author, 
     * and publication details.</p>
     *
     * @param book the {@link Book} object to be added to the library
     * @return the saved {@link Book} entity with any auto-generated fields populated (e.g., ID)
     */
    public Book addBook(Book book) {
        return bookRepository.save(book);
    }

    /**
     * Updates an existing book's details.
     *
     * <p>When called, this method updates the properties of an existing book 
     * using the new data passed in {@code bookDetails}. If the book with the 
     * specified ID does not exist, a {@link ResourceNotFoundException} is thrown.</p>
     *
     * @param bookId the unique identifier of the book to update
     * @param bookDetails the new details to be applied to the book
     * @return the updated {@link Book} object after saving the changes
     * @throws ResourceNotFoundException if the book with the specified ID does not exist
     */
    public Book updateBook(int bookId, Book bookDetails) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book with ID " + bookId + " not found"));
        
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
     * Deletes a book from the library by its ID.
     *
     * <p>If the book with the specified ID exists, it is removed from the system. 
     * If not, a {@link ResourceNotFoundException} is thrown.</p>
     *
     * @param bookId the unique identifier of the book to delete
     * @throws ResourceNotFoundException if the book with the specified ID is not found
     */
    public void deleteBook(int bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book with ID " + bookId + " not found"));
        bookRepository.delete(book);
    }
}
