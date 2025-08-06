package com.libraryman_api.borrowing;

import com.libraryman_api.TestUtil;
import com.libraryman_api.TestUtil.Constant;
import com.libraryman_api.book.BookDto;
import com.libraryman_api.book.BookService;
import com.libraryman_api.exception.InvalidSortFieldException;
import com.libraryman_api.exception.ResourceNotFoundException;
import com.libraryman_api.fine.Fine;
import com.libraryman_api.fine.FineRepository;
import com.libraryman_api.member.MemberService;
import com.libraryman_api.notification.NotificationService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mapping.PropertyReferenceException;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

/**
 * Tests the {@link BorrowingService}.
 */
@ExtendWith(MockitoExtension.class)
class BorrowingServiceTest {
    @Mock
    private BorrowingRepository borrowingRepository;
    @Mock
    private FineRepository fineRepository;
    @Mock
    private NotificationService notificationService;
    @Mock
    private BookService bookService;
    @Mock
    private MemberService memberService;
    @InjectMocks
    private BorrowingService borrowingService;

    @Nested
    class GetAllBorrowings {
        @Test
        void success() {
            Pageable pageable = TestUtil.getPageRequest("dueDate");
            Borrowings borrowings = TestUtil.getBorrowings();
            when(bookService.EntityToDto(any())).thenCallRealMethod();
            Page<Borrowings> borrowingsPage = new PageImpl<>(List.of(borrowings));
            when(borrowingRepository.findAll(pageable)).thenReturn(borrowingsPage);

            Page<BorrowingsDto> borrowingsDtoPage = borrowingService.getAllBorrowings(pageable);

            assertEquals(1, borrowingsDtoPage.getTotalElements());
            assertEquals(Constant.BOOK_TITLE, borrowingsDtoPage.getContent().get(0).getBook().getTitle());
        }

        @Test
        void invalidSortField_throwsException() {
            Pageable pageable = TestUtil.getPageRequest("dueDate");
            when(borrowingRepository.findAll(pageable)).thenThrow(PropertyReferenceException.class);

            Exception exception = assertThrows(InvalidSortFieldException.class, () -> borrowingService.getAllBorrowings(pageable));

            assertEquals("The specified 'sortBy' value is invalid.", exception.getMessage());
        }
    }

    @Nested
    class GetBorrowingById {
        @Test
        void success() {
            Borrowings borrowings = TestUtil.getBorrowings();
            when(borrowingRepository.findById(any())).thenReturn(Optional.of(borrowings));
            when(bookService.EntityToDto(any())).thenCallRealMethod();
            Optional<BorrowingsDto> borrowingsDto = borrowingService.getBorrowingById(Constant.BORROWING_ID);

            assertTrue(borrowingsDto.isPresent());
            assertEquals(Constant.BOOK_TITLE, borrowingsDto.get().getBook().getTitle());
        }

        @Test
        void noBorrowingsFound_returnsEmpty() {
            when(borrowingRepository.findById(any())).thenReturn(Optional.empty());

            Optional<BorrowingsDto> borrowingsDto = borrowingService.getBorrowingById(Constant.BORROWING_ID);

            assertTrue(borrowingsDto.isEmpty());
        }
    }

    @Nested
    class BorrowBook {
        @Test
        void success() {
            when(bookService.getBookById(anyInt())).thenReturn(Optional.of(TestUtil.getBookDto()));
            when(bookService.DtoToEntity(any())).thenCallRealMethod();
            when(bookService.EntityToDto(any())).thenCallRealMethod();
            when(memberService.getMemberById(anyInt())).thenReturn(Optional.of(TestUtil.getMembersDto()));
            when(memberService.DtoEntity(any())).thenCallRealMethod();
            when(memberService.EntityToDto(any())).thenCallRealMethod();
            when(borrowingRepository.save(any())).thenReturn(TestUtil.getBorrowings());

            BorrowingsDto borrowingsDto = borrowingService.borrowBook(TestUtil.getBorrowingsDto());

            assertEquals(Constant.BOOK_TITLE, borrowingsDto.getBook().getTitle());
        }

        @Test
        void noCopiesAvailable_throwsException() {
            BookDto bookDto = TestUtil.getBookDto();
            bookDto.setCopiesAvailable(0);
            when(bookService.getBookById(anyInt())).thenReturn(Optional.of(bookDto));
            when(bookService.DtoToEntity(any())).thenCallRealMethod();
            when(memberService.getMemberById(anyInt())).thenReturn(Optional.of(TestUtil.getMembersDto()));
            when(memberService.DtoEntity(any())).thenCallRealMethod();

            Exception exception = assertThrows(ResourceNotFoundException.class, () -> borrowingService.borrowBook(TestUtil.getBorrowingsDto()));

            assertEquals("Not enough copies available", exception.getMessage());
        }

        @Test
        void noBookFound_throwsException() {
            when(bookService.getBookById(anyInt())).thenReturn(Optional.empty());
            when(memberService.getMemberById(anyInt())).thenReturn(Optional.of(TestUtil.getMembersDto()));

            Exception exception = assertThrows(ResourceNotFoundException.class, () -> borrowingService.borrowBook(TestUtil.getBorrowingsDto()));

            assertEquals("Book not found", exception.getMessage());
        }

        @Test
        void noMemberFound_throwsException() {
            when(bookService.getBookById(anyInt())).thenReturn(Optional.of(TestUtil.getBookDto()));
            when(memberService.getMemberById(anyInt())).thenReturn(Optional.empty());

            Exception exception = assertThrows(ResourceNotFoundException.class, () -> borrowingService.borrowBook(TestUtil.getBorrowingsDto()));

            assertEquals("Member not found", exception.getMessage());
        }
    }

    @Nested
    class ReturnBook {
        @Test
        void success() {
            Borrowings borrowings = TestUtil.getBorrowings();
            borrowings.setDueDate(Date.from(LocalDate.now().plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
            when(borrowingRepository.findById(anyInt())).thenReturn(Optional.of(borrowings));
            when(borrowingRepository.save(any())).thenReturn(borrowings);

            when(memberService.getMemberById(anyInt())).thenReturn(Optional.of(TestUtil.getMembersDto()));
            when(memberService.DtoEntity(any())).thenCallRealMethod();
            when(memberService.EntityToDto(any())).thenCallRealMethod();

            when(bookService.getBookById(anyInt())).thenReturn(Optional.of(TestUtil.getBookDto()));
            when(bookService.DtoToEntity(any())).thenCallRealMethod();
            when(bookService.EntityToDto(any())).thenCallRealMethod();

            BorrowingsDto borrowingsDto = borrowingService.returnBook(Constant.BORROWING_ID);

            assertEquals(Constant.BOOK_TITLE, borrowingsDto.getBook().getTitle());
            assertNotNull(borrowingsDto.getDueDate());
        }

        @Test
        void noBorrowingsFound_throwsException() {
            when(borrowingRepository.findById(anyInt())).thenReturn(Optional.empty());

            Exception exception = assertThrows(ResourceNotFoundException.class, () -> borrowingService.returnBook(Constant.BORROWING_ID));

            assertEquals("Borrowing not found", exception.getMessage());
        }

        @Test
        void noMemberFound_throwsException() {
            Borrowings borrowings = TestUtil.getBorrowings();
            borrowings.setDueDate(Date.from(LocalDate.now().plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
            when(borrowingRepository.findById(anyInt())).thenReturn(Optional.of(borrowings));
            when(memberService.getMemberById(anyInt())).thenReturn(Optional.empty());
            when(memberService.EntityToDto(any())).thenCallRealMethod();
            when(bookService.EntityToDto(any())).thenCallRealMethod();

            Exception exception = assertThrows(ResourceNotFoundException.class, () -> borrowingService.returnBook(Constant.BORROWING_ID));

            assertEquals("Member not found", exception.getMessage());
        }

        @Test
        void bookAlreadyReturned_throwsException() {
            Borrowings borrowings = TestUtil.getBorrowings();
            borrowings.setDueDate(Date.from(LocalDate.now().plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
            borrowings.setReturnDate(new Date());
            when(borrowingRepository.findById(anyInt())).thenReturn(Optional.of(borrowings));
            when(memberService.getMemberById(anyInt())).thenReturn(Optional.of(TestUtil.getMembersDto()));
            when(memberService.EntityToDto(any())).thenCallRealMethod();

            Exception exception = assertThrows(ResourceNotFoundException.class, () -> borrowingService.returnBook(Constant.BORROWING_ID));

            assertEquals("Book has already been returned", exception.getMessage());
        }

        @Nested
        class DueDateMissed {
            @Test
            void noFine_throwsException() {
                Borrowings borrowings = TestUtil.getBorrowings();
                borrowings.setDueDate(Date.from(LocalDate.now().minusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
                when(borrowingRepository.findById(anyInt())).thenReturn(Optional.of(borrowings));
                when(borrowingRepository.save(any())).thenReturn(borrowings);
                when(memberService.getMemberById(anyInt())).thenReturn(Optional.of(TestUtil.getMembersDto()));
                when(memberService.DtoEntity(any())).thenCallRealMethod();
                when(memberService.EntityToDto(any())).thenCallRealMethod();

                Exception exception = assertThrows(ResourceNotFoundException.class, () -> borrowingService.returnBook(Constant.BORROWING_ID));

                assertEquals("Due date passed. Fine imposed, pay fine first to return the book", exception.getMessage());
            }

            @Test
            void hasOutstandingFine_throwsException() {
                Fine fine = TestUtil.getFine();
                Borrowings borrowings = TestUtil.getBorrowings();
                borrowings.setDueDate(Date.from(LocalDate.now().minusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
                borrowings.setFine(fine);
                when(borrowingRepository.findById(anyInt())).thenReturn(Optional.of(borrowings));
                when(memberService.getMemberById(anyInt())).thenReturn(Optional.of(TestUtil.getMembersDto()));
                when(memberService.DtoEntity(any())).thenCallRealMethod();
                when(memberService.EntityToDto(any())).thenCallRealMethod();

                Exception exception = assertThrows(ResourceNotFoundException.class, () -> borrowingService.returnBook(Constant.BORROWING_ID));

                assertEquals("Outstanding fine, please pay before returning the book", exception.getMessage());
            }

            @Test
            void hasPaidFine_successfullyReturned() {
                Fine fine = TestUtil.getFine();
                fine.setPaid(true);
                Borrowings borrowings = TestUtil.getBorrowings();
                borrowings.setDueDate(Date.from(LocalDate.now().minusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
                borrowings.setFine(fine);
                when(borrowingRepository.findById(anyInt())).thenReturn(Optional.of(borrowings));
                when(borrowingRepository.save(any())).thenReturn(borrowings);
                when(memberService.getMemberById(anyInt())).thenReturn(Optional.of(TestUtil.getMembersDto()));
                when(memberService.DtoEntity(any())).thenCallRealMethod();
                when(memberService.EntityToDto(any())).thenCallRealMethod();
                when(bookService.getBookById(anyInt())).thenReturn(Optional.of(TestUtil.getBookDto()));
                when(bookService.DtoToEntity(any())).thenCallRealMethod();
                when(bookService.EntityToDto(any())).thenCallRealMethod();

                BorrowingsDto borrowingsDto = borrowingService.returnBook(Constant.BORROWING_ID);

                assertEquals(Constant.BOOK_TITLE, borrowingsDto.getBook().getTitle());
                assertNotNull(borrowingsDto.getDueDate());
            }
        }
    }

    @Nested
    class PayFine {
        @Test
        void success() {
            Borrowings borrowings = TestUtil.getBorrowings();
            borrowings.setFine(TestUtil.getFine());
            borrowings.setDueDate(Date.from(LocalDate.now().plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
            when(borrowingRepository.findById(anyInt())).thenReturn(Optional.of(borrowings));
            when(borrowingRepository.save(any())).thenReturn(borrowings);
            when(memberService.getMemberById(anyInt())).thenReturn(Optional.of(TestUtil.getMembersDto()));
            when(memberService.EntityToDto(any())).thenCallRealMethod();
            when(bookService.EntityToDto(any())).thenCallRealMethod();

            String result = borrowingService.payFine(Constant.BORROWING_ID);

            assertEquals("PAID", result);
        }

        @Test
        void borrowingNotFound_throwsException() {
            when(borrowingRepository.findById(anyInt())).thenReturn(Optional.empty());

            Exception exception = assertThrows(ResourceNotFoundException.class, () -> borrowingService.payFine(Constant.BORROWING_ID));

            assertEquals("Borrowing not found", exception.getMessage());
        }

        @Test
        void memberNotFound_throwsException() {
            when(borrowingRepository.findById(anyInt())).thenReturn(Optional.of(TestUtil.getBorrowings()));
            when(memberService.getMemberById(anyInt())).thenReturn(Optional.empty());
            when(memberService.EntityToDto(any())).thenCallRealMethod();
            when(bookService.EntityToDto(any())).thenCallRealMethod();

            Exception exception = assertThrows(ResourceNotFoundException.class, () -> borrowingService.payFine(Constant.BORROWING_ID));

            assertEquals("Member not found", exception.getMessage());
        }

        @Test
        void nullFine_throwsException() {
            Borrowings borrowings = TestUtil.getBorrowings();
            borrowings.setDueDate(Date.from(LocalDate.now().plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
            when(borrowingRepository.findById(anyInt())).thenReturn(Optional.of(borrowings));
            when(memberService.getMemberById(anyInt())).thenReturn(Optional.of(TestUtil.getMembersDto()));
            when(memberService.EntityToDto(any())).thenCallRealMethod();
            when(bookService.EntityToDto(any())).thenCallRealMethod();

            Exception exception = assertThrows(ResourceNotFoundException.class, () -> borrowingService.payFine(Constant.BORROWING_ID));

            assertEquals("No outstanding fine found or fine already paid", exception.getMessage());
        }

        @Test
        void fineAlreadyPaid_throwsException() {
            Fine fine = TestUtil.getFine();
            fine.setPaid(true);
            Borrowings borrowings = TestUtil.getBorrowings();
            borrowings.setDueDate(Date.from(LocalDate.now().plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
            when(borrowingRepository.findById(anyInt())).thenReturn(Optional.of(borrowings));
            when(memberService.getMemberById(anyInt())).thenReturn(Optional.of(TestUtil.getMembersDto()));
            when(memberService.EntityToDto(any())).thenCallRealMethod();
            when(bookService.EntityToDto(any())).thenCallRealMethod();

            Exception exception = assertThrows(ResourceNotFoundException.class, () -> borrowingService.payFine(Constant.BORROWING_ID));

            assertEquals("No outstanding fine found or fine already paid", exception.getMessage());
        }
    }

    @Nested
    class UpdateBookCopies {

        @Nested
        class Add {
            @Test
            void success() {
                BookDto bookDto = TestUtil.getBookDto();
                when(bookService.DtoToEntity(any())).thenCallRealMethod();

                assertDoesNotThrow(() -> borrowingService.updateBookCopies(Optional.of(bookDto), "ADD", 1));
            }

            @Test
            void bookNotPresent_throwsException() {
                Exception exception = assertThrows(ResourceNotFoundException.class, () -> borrowingService.updateBookCopies(Optional.empty(), "ADD", 1));

                assertEquals("Book not found", exception.getMessage());
            }
        }

        @Nested
        class Remove {
            @Test
            void success() {
                BookDto bookDto = TestUtil.getBookDto();
                when(bookService.DtoToEntity(any())).thenCallRealMethod();

                assertDoesNotThrow(() -> borrowingService.updateBookCopies(Optional.of(bookDto), "REMOVE", 1));
            }

            @Test
            void bookNotPresent_throwsException() {
                Exception exception = assertThrows(ResourceNotFoundException.class, () -> borrowingService.updateBookCopies(Optional.empty(), "REMOVE", 1));

                assertEquals("Book not found", exception.getMessage());
            }

            @Test
            void notEnoughCopies_throwsException() {
                BookDto bookDto = TestUtil.getBookDto();
                bookDto.setCopiesAvailable(0);
                when(bookService.DtoToEntity(any())).thenCallRealMethod();

                Exception exception = assertThrows(ResourceNotFoundException.class, () -> borrowingService.updateBookCopies(Optional.of(bookDto), "REMOVE", 1));

                assertEquals("Not enough copies are available", exception.getMessage());
            }
        }
    }

    @Nested
    class GetAllBorrowingsOfMember {
        @Test
        void success() {
            Pageable pageable = TestUtil.getPageRequest("dueDate");
            Borrowings borrowings = TestUtil.getBorrowings();
            Page<Borrowings> borrowingsPage = new PageImpl<>(List.of(borrowings));
            when(borrowingRepository.findByMember_memberId(anyInt(), any())).thenReturn(borrowingsPage);
            when(bookService.EntityToDto(any())).thenCallRealMethod();
            when(memberService.EntityToDto(any())).thenCallRealMethod();

            Page<BorrowingsDto> borrowingsDtoPage = borrowingService.getAllBorrowingsOfMember(Constant.MEMBER_ID, pageable);

            assertEquals(Constant.BOOK_TITLE, borrowingsDtoPage.getContent().get(0).getBook().getTitle());
        }

        @Test
        void memberNotFound_throwsException() {
            Pageable pageable = TestUtil.getPageRequest("dueDate");
            when(borrowingRepository.findByMember_memberId(anyInt(), any())).thenReturn(new PageImpl<>(List.of()));

            Exception exception = assertThrows(ResourceNotFoundException.class, () -> borrowingService.getAllBorrowingsOfMember(Constant.MEMBER_ID, pageable));

            assertEquals("Member didn't borrow any book", exception.getMessage());
        }

        @Test
        void sortByFieldInvalid_throwsException() {
            Pageable pageable = TestUtil.getPageRequest("nonexistentField");
            when(borrowingRepository.findByMember_memberId(anyInt(), any())).thenThrow(PropertyReferenceException.class);

            Exception exception = assertThrows(InvalidSortFieldException.class, () -> borrowingService.getAllBorrowingsOfMember(Constant.MEMBER_ID, pageable));

            assertEquals("The specified 'sortBy' value is invalid.", exception.getMessage());
        }
    }
}