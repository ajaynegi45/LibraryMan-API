package com.libraryman_api.book;

import com.libraryman_api.TestUtil;
import com.libraryman_api.TestUtil.Constant;
import com.libraryman_api.exception.InvalidSortFieldException;
import com.libraryman_api.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mapping.PropertyReferenceException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

/**
 * Tests the {@link BookService} class.
 */
@ExtendWith(MockitoExtension.class)
class BookServiceTest {
    @Mock
    private BookRepository bookRepository;
    @InjectMocks
    private BookService bookService;

    @Nested
    class GetAllBooks {
        @Test
        void success() {
            Pageable pageable = TestUtil.getPageRequest("title");
            when(bookRepository.findAll(pageable)).thenReturn(TestUtil.getBookPage());

            Page<BookDto> bookDtoPage = bookService.getAllBooks(pageable);

            assertEquals(1, bookDtoPage.getTotalElements());
            assertEquals(Constant.BOOK_TITLE, bookDtoPage.getContent().get(0).getTitle());
        }

        @Test
        void invalidSortField_throwsException() {
            Pageable pageable = TestUtil.getPageRequest("nonexistentField");
            when(bookRepository.findAll(pageable)).thenThrow(PropertyReferenceException.class);

            Exception exception = assertThrows(InvalidSortFieldException.class, () -> bookService.getAllBooks(pageable));

            assertEquals("The specified 'sortBy' value is invalid.", exception.getMessage());
        }
    }

    @Nested
    class GetBookById {
        @Test
        void success() {
            when(bookRepository.findById(any())).thenReturn(Optional.of(TestUtil.getBook()));

            Optional<BookDto> bookDto = bookService.getBookById(Constant.BOOK_ID);

            assertTrue(bookDto.isPresent());
            assertEquals(Constant.BOOK_TITLE, bookDto.get().getTitle());
        }

        @Test
        void noBookFound_returnsEmpty() {
            int idNotInRepository = TestUtil.getRandomInt();
            when(bookRepository.findById(anyInt())).thenReturn(Optional.empty());

            Optional<BookDto> bookDto = bookService.getBookById(idNotInRepository);

            assertTrue(bookDto.isEmpty());
        }
    }

    @Test
    void addBook() {
        BookDto bookDto = TestUtil.getBookDto();
        when(bookRepository.save(any())).thenReturn(TestUtil.getBook());

        BookDto bookDtoResult = bookService.addBook(bookDto);

        assertEquals(Constant.BOOK_TITLE, bookDtoResult.getTitle());
    }

    @Nested
    class UpdateBook {
        @Test
        void success() {
            BookDto bookDtoDetails = TestUtil.getBookDto();
            bookDtoDetails.setTitle("New Title");
            Book existingBook = TestUtil.getBook();
            existingBook.setTitle("Existing Title");
            Book savedBook = TestUtil.getBook();
            savedBook.setTitle(bookDtoDetails.getTitle());
            when(bookRepository.findById(any())).thenReturn(Optional.of(existingBook));
            when(bookRepository.save(any())).thenReturn(savedBook);

            BookDto bookDtoResult = bookService.updateBook(Constant.BOOK_ID, bookDtoDetails);

            assertEquals(bookDtoDetails.getTitle(), bookDtoResult.getTitle());
        }

        @Test
        void noBookFound_throwsException() {
            int idNotInRepository = TestUtil.getRandomInt();
            BookDto newBookDto = TestUtil.getBookDto();
            when(bookRepository.findById(any())).thenReturn(Optional.empty());

            Exception exception = assertThrows(ResourceNotFoundException.class, () -> bookService.updateBook(idNotInRepository, newBookDto));

            assertEquals("Book not found", exception.getMessage());
        }
    }

    @Nested
    class DeleteBook {
        @Test
        void success() {
            when(bookRepository.findById(any())).thenReturn(Optional.of(TestUtil.getBook()));

            assertDoesNotThrow(() -> bookService.deleteBook(Constant.BOOK_ID));
        }

        @Test
        void noBookFound_throwsException() {
            int idNotInRepository = TestUtil.getRandomInt();
            when(bookRepository.findById(anyInt())).thenReturn(Optional.empty());

            Exception exception = assertThrows(ResourceNotFoundException.class, () -> bookService.deleteBook(idNotInRepository));

            assertEquals("Book not found", exception.getMessage());
        }
    }

    @Test
    void entityToDto() {
        Book book = TestUtil.getBook();

        BookDto bookDto = bookService.EntityToDto(book);

        assertEquals(book.getBookId(), bookDto.getBookId());
        assertEquals(book.getPublisher(), bookDto.getPublisher());
        assertEquals(book.getPublishedYear(), bookDto.getPublishedYear());
        assertEquals(book.getTitle(), bookDto.getTitle());
        assertEquals(book.getAuthor(), bookDto.getAuthor());
        assertEquals(book.getGenre(), bookDto.getGenre());
        assertEquals(book.getIsbn(), bookDto.getIsbn());
        assertEquals(book.getCopiesAvailable(), bookDto.getCopiesAvailable());
    }

    @Test
    void dtoToEntity() {
        BookDto bookDto = TestUtil.getBookDto();

        Book book = bookService.DtoToEntity(bookDto);

        assertEquals(bookDto.getBookId(), book.getBookId());
        assertEquals(bookDto.getPublisher(), book.getPublisher());
        assertEquals(bookDto.getPublishedYear(), book.getPublishedYear());
        assertEquals(bookDto.getTitle(), book.getTitle());
        assertEquals(bookDto.getAuthor(), book.getAuthor());
        assertEquals(bookDto.getGenre(), book.getGenre());
        assertEquals(bookDto.getIsbn(), book.getIsbn());
        assertEquals(bookDto.getCopiesAvailable(), book.getCopiesAvailable());
    }

    @Test
    void searchBook() {
        Pageable pageable = TestUtil.getPageRequest("title");
        when(bookRepository.searchBook("keyword", pageable)).thenReturn(TestUtil.getBookPage());

        Page<Book> bookPage = bookService.searchBook("keyword", pageable);

        assertEquals(Constant.BOOK_TITLE, bookPage.getContent().get(0).getTitle());
    }
}