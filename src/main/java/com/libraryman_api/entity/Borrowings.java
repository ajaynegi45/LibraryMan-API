package com.libraryman_api.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class Borrowings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "borrowing_id")
    private int borrowingId;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Books book;

    @OneToOne
    @JoinColumn(name = "fine_id")
    private Fines fine;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Members member;

    @Column(name = "borrow_date",nullable = false)
    private Date borrowDate;

    @Column(name = "due_date", nullable = false)
    private Date dueDate;

    @Column(name = "return_date")
    private Date returnDate;


    public Borrowings() {
    }

    public Borrowings(Books book, Members member, Date borrowDate, Date dueDate, Date returnDate) {
        this.book = book;
        this.member = member;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
    }

    public Fines getFine() {
        return fine;
    }

    public void setFine(Fines fine) {
        this.fine = fine;
    }

    public int getBorrowingId() {
        return borrowingId;
    }

    public Books getBook() {
        return book;
    }

    public void setBook(Books book) {
        this.book = book;
    }

    public Members getMember() {
        return member;
    }

    public void setMember(Members member) {
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
}