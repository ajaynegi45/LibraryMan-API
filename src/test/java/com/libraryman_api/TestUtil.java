package com.libraryman_api;

import com.libraryman_api.book.Book;
import com.libraryman_api.book.BookDto;
import com.libraryman_api.borrowing.Borrowings;
import com.libraryman_api.borrowing.BorrowingsDto;
import com.libraryman_api.fine.Fine;
import com.libraryman_api.member.Members;
import com.libraryman_api.member.Role;
import com.libraryman_api.member.dto.MembersDto;
import com.libraryman_api.member.dto.UpdateMembersDto;
import com.libraryman_api.member.dto.UpdatePasswordDto;
import com.libraryman_api.newsletter.NewsletterSubscriber;
import com.libraryman_api.security.model.LoginRequest;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class TestUtil {
    private static final Random randomNumberUtils = new Random();

    public static int getRandomInt() {
        return randomNumberUtils.nextInt(100, 1000);
    }

    public static Book getBook() {
        Book book = new Book();
        book.setBookId(Constant.BOOK_ID);
        book.setTitle(Constant.BOOK_TITLE);
        book.setAuthor("Sarah Maas");
        book.setIsbn("978-0-7475-3269-9");
        book.setPublisher("Penguin Random House");
        book.setPublishedYear(2025);
        book.setGenre("Fiction");
        book.setCopiesAvailable(5);
        return book;
    }

    public static BookDto getBookDto() {
        BookDto inputDto = new BookDto();
        inputDto.setBookId(Constant.BOOK_ID);
        inputDto.setTitle(Constant.BOOK_TITLE);
        inputDto.setAuthor("Sarah Maas");
        inputDto.setIsbn("978-0-7475-3269-9");
        inputDto.setPublisher("Penguin Random House");
        inputDto.setPublishedYear(2025);
        inputDto.setGenre("Fiction");
        inputDto.setCopiesAvailable(5);
        return inputDto;
    }

    public static BorrowingsDto getBorrowingsDto() {
        BorrowingsDto borrowingsDto = new BorrowingsDto();
        borrowingsDto.setBorrowingId(1);
        borrowingsDto.setBook(getBookDto());
        borrowingsDto.setFine(null);
        borrowingsDto.setMember(getMembersDto());
        borrowingsDto.setBorrowDate(new Date());
        borrowingsDto.setReturnDate(adjustDays(new Date(), 7));
        return borrowingsDto;
    }

    public static Fine getFine() {
        Fine fine = new Fine();
        fine.setAmount(BigDecimal.valueOf(100.00));
        fine.setPaid(false);
        return fine;
    }

    public static MembersDto getMembersDto() {
        MembersDto membersDto = new MembersDto();
        membersDto.setMemberId(Constant.MEMBER_ID);
        membersDto.setName("Jane Doe");
        membersDto.setUsername("Jane01");
        membersDto.setEmail("jane.doe@gmail.com");
        membersDto.setPassword("password");
        membersDto.setRole(Role.USER);
        membersDto.setMembershipDate(new Date());
        return membersDto;
    }

    public static UpdateMembersDto getUpdateMembersDto() {
        UpdateMembersDto updateMembersDto = new UpdateMembersDto();
        updateMembersDto.setName("David Green");
        updateMembersDto.setUsername("DGreen");
        updateMembersDto.setEmail("david.green@gmail.com");
        return updateMembersDto;
    }

    public static Borrowings getBorrowings() {
        Borrowings borrowings = new Borrowings();
        borrowings.setBook(getBook());
        borrowings.setMember(getMembers());
        borrowings.setBorrowDate(new Date());
        borrowings.setDueDate(adjustDays(new Date(), 14));
        borrowings.setReturnDate(null);
        return borrowings;
    }

    public static Date adjustDays(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, days);
        return calendar.getTime();
    }

    public static Pageable getPageRequest(String sortBy) {
        return PageRequest.of(0, 10, Sort.by(sortBy));
    }

    public static Page<Book> getBookPage() {
        return new PageImpl<>(List.of(getBook()));
    }

    public static Members getMembers() {
        Members members = new Members();
        members.setMemberId(Constant.MEMBER_ID);
        members.setUsername("John01");
        members.setName("John Doe");
        members.setEmail("john@gmail.com");
        members.setPassword("password");
        members.setRole(Role.USER);
        members.setMembershipDate(new Date());
        return members;
    }

    public static UpdatePasswordDto getUpdatePasswordDto() {
        UpdatePasswordDto updatePasswordDto = new UpdatePasswordDto();
        updatePasswordDto.setCurrentPassword("password");
        updatePasswordDto.setNewPassword("newPassword");
        return updatePasswordDto;
    }

    public static NewsletterSubscriber getNewsletterSubscriber() {
        NewsletterSubscriber newsletterSubscriber = new NewsletterSubscriber();
        newsletterSubscriber.setEmail("emma@hotmail.com");
        newsletterSubscriber.setActive(false);
        return newsletterSubscriber;
    }

    public static LoginRequest getLoginRequest() {
        LoginRequest request = new LoginRequest();
        request.setUsername("username");
        request.setPassword("password");
        return request;
    }

    public static class Constant {
        public static final int BOOK_ID = 11;
        public static final int BORROWING_ID = 22;
        public static final int MEMBER_ID = 33;
        public static final String BOOK_TITLE = "Test Book";
    }
}
