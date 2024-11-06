package com.libraryman_api.borrowing;

import com.libraryman_api.book.BookDto;
import com.libraryman_api.fine.Fines;
import com.libraryman_api.member.dto.MembersDto;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

public class BorrowingsDto {

    private int borrowingId;
    @NotNull(message = "Book is required")
    private BookDto book;
    private Fines fine;
    @NotNull(message = "Member is required")
    private MembersDto member;

    private Date borrowDate;
    private Date dueDate;
    private Date returnDate;



    public BorrowingsDto(int borrowingId, BookDto book, Fines fine, MembersDto member, Date borrowDate, Date dueDate, Date returnDate) {
        this.borrowingId = borrowingId;
        this.book = book;
        this.fine = fine;
        this.member = member;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
    }

    public BorrowingsDto() {
    }

    public int getBorrowingId() {
        return borrowingId;
    }

    public void setBorrowingId(int borrowingId) {
        this.borrowingId = borrowingId;
    }

    public BookDto getBook() {
        return book;
    }

    public void setBook(BookDto book) {
        this.book = book;
    }

    public Fines getFine() {
        return fine;
    }

    public void setFine(Fines fine) {
        this.fine = fine;
    }

    public MembersDto getMember() {
        return member;
    }

    public void setMember(MembersDto member) {
        this.member = member;
    }

    public Date getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(Date borrowDate) {
        this.borrowDate = borrowDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    @Override
    public String toString() {
        return "BorrowingsDto{" +
                "borrowingId=" + borrowingId +
                ", book=" + book +
                ", fine=" + fine +
                ", member=" + member +
                ", borrowDate=" + borrowDate +
                ", dueDate=" + dueDate +
                ", returnDate=" + returnDate +
                '}';
    }
}
