package com.libraryman_api.book;

import com.libraryman_api.exception.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private ModelMapper mapper;

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
    public List<BookDto> getAllBooks() {
        List<Book> allBooks = bookRepository.findAll();
         return allBooks.stream().map(this::EntityToDto).toList();
    }

    /**
     * Retrieves a book by its ID.
     *
     * @param bookId the ID of the book to retrieve
     * @return an {@code Optional} containing the found book, or {@code Optional.empty()} if no book was found
     */
    public Optional<BookDto> getBookById(int bookId) {

        Optional<Book> getBook = bookRepository.findById(bookId);
        return getBook.map(this::EntityToDto);
    }

    /**
     * Adds a new book to the database.
     *
     * @param bookDto the book to be added
     * @return the saved book
     */
    public BookDto addBook(BookDto bookDto) {
        Book book = DtoToEntity(bookDto);
        Book savedBook = bookRepository.save(book);
        return EntityToDto(savedBook);
    }

    /**
     * Updates an existing book with the given details.
     *
     * @param bookId the ID of the book to update
     * @param bookDtoDetails the new details for the book
     * @return the updated book
     * @throws ResourceNotFoundException if the book with the specified ID is not found
     */
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
    public void deleteBook(int bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));
        bookRepository.delete(book);
    }



    public BookDto EntityToDto(Book book){
        return mapper.map(book,BookDto.class);
    }
    public Book DtoToEntity(BookDto bookDto){
        return mapper.map(bookDto,Book.class);
    }

}
