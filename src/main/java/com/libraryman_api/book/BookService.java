package com.libraryman_api.book;

import com.libraryman_api.exception.InvalidSortFieldException;
import com.libraryman_api.exception.ResourceNotFoundException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.stereotype.Service;

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
     * Retrieves a paginated list of all books from the database.
     *
     * @param pageable the pagination information, including the page number and size
     * @return a {@link Page} of {@link Book} representing all books
     * @throws InvalidSortFieldException if an invalid sortBy field is specified
     */

    @Cacheable(value = "books")
    public Page<BookDto> getAllBooks(Pageable pageable) {
        try {
            Page<Book> pagedBooks = bookRepository.findAll(pageable);
            return pagedBooks.map(this::EntityToDto);
        } catch (PropertyReferenceException ex) {
            throw new InvalidSortFieldException("The specified 'sortBy' value is invalid.");
        }
    }

    /**
     * Retrieves a book by its ID.
     *
     * @param bookId the ID of the book to retrieve
     * @return an {@code Optional} containing the found book, or {@code Optional.empty()} if no book was found
     */

    @Cacheable(value = "books", key = "#bookId")
    public Optional<BookDto> getBookById(int bookId) {

        Optional<Book> bookById = bookRepository.findById(bookId);
        return bookById.map(this::EntityToDto);
    }

    /**
     * Adds a new book to the database.
     *
     * @param bookDto the book to be added
     * @return the saved book
     */

    @CacheEvict(value = "books", allEntries = true)
    public BookDto addBook(BookDto bookDto) {
        Book book = DtoToEntity(bookDto);
        Book savedBook = bookRepository.save(book);
        return EntityToDto(savedBook);
    }

    /**
     * Updates an existing book with the given details.
     *
     * @param bookId         the ID of the book to update
     * @param bookDtoDetails the new details for the book
     * @return the updated book
     * @throws ResourceNotFoundException if the book with the specified ID is not found
     */

    @Caching(evict = {
            @CacheEvict(value = "books", key = "#bookId"), // Evict the specific book cache
            @CacheEvict(value = "books", allEntries = true) // Evict the list cache
    })
    public BookDto updateBook(int bookId, BookDto bookDtoDetails) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));
        book.setTitle(bookDtoDetails.getTitle());
        book.setAuthor(bookDtoDetails.getAuthor());
        book.setIsbn(bookDtoDetails.getIsbn());
        book.setPublisher(bookDtoDetails.getPublisher());
        book.setPublishedYear(bookDtoDetails.getPublishedYear());
        book.setGenre(bookDtoDetails.getGenre());
        book.setCopiesAvailable(bookDtoDetails.getCopiesAvailable());
        Book updatedBook = bookRepository.save(book);
        return EntityToDto(updatedBook);
    }

    /**
     * Deletes a book by its ID.
     *
     * @param bookId the ID of the book to delete
     * @throws ResourceNotFoundException if the book with the specified ID is not found
     */

    @Caching(evict = {
            @CacheEvict(value = "books", key = "#bookId"), // Evict the specific book cache
            @CacheEvict(value = "books", allEntries = true) // Evict the list cache
    })
    public void deleteBook(int bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));
        bookRepository.delete(book);
    }

    /**
     * Converts a Book entity to a BookDto object.
     *
     * <p>This method takes a Book entity and transforms it into a BookDto object for
     * data transfer between application layers. It maps all relevant book details,
     * including book ID, publisher, published year, title, author, genre, ISBN,
     * and copies available, from the entity to the DTO.</p>
     *
     * @param book the entity object containing book information
     * @return a BookDto object with data populated from the entity
     */

    public BookDto EntityToDto(Book book) {
        BookDto bookDto = new BookDto();
        bookDto.setBookId(book.getBookId());
        bookDto.setPublisher(book.getPublisher());
        bookDto.setPublishedYear(book.getPublishedYear());
        bookDto.setTitle(book.getTitle());
        bookDto.setAuthor(book.getAuthor());
        bookDto.setGenre(book.getGenre());
        bookDto.setIsbn(book.getIsbn());
        bookDto.setCopiesAvailable(book.getCopiesAvailable());
        return bookDto;
    }

    /**
     * Converts a BookDto object to a Book entity.
     *
     * <p>This method takes a BookDto object and converts it into a Book entity for
     * use in database operations. It maps all relevant book details, including
     * book ID, author, genre, publisher, published year, title, ISBN, and copies
     * available, from the DTO to the entity.</p>
     *
     * @param bookDto the DTO object containing book information
     * @return a Book entity with data populated from the DTO
     */


    public Book DtoToEntity(BookDto bookDto) {
        Book book = new Book();
        book.setBookId(bookDto.getBookId());
        book.setAuthor(bookDto.getAuthor());
        book.setGenre(bookDto.getGenre());
        book.setPublisher(bookDto.getPublisher());
        book.setPublishedYear(bookDto.getPublishedYear());
        book.setTitle(bookDto.getTitle());
        book.setCopiesAvailable(bookDto.getCopiesAvailable());
        book.setIsbn(bookDto.getIsbn());
        return book;
    }
    
    /**
     * <p>This method takes String keyword and search book based on 
     * title, author, genre, publishedYear, etc. from Book Entity. </p>
     * 
     * @param keyword
     * @param pageable
     * @return
     */
    public Page<Book> searchBook(String keyword,Pageable pageable ){
    	return bookRepository.searchBook(keyword,pageable);
    }
}
