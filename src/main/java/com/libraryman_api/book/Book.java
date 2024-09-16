package com.libraryman_api.book;

import jakarta.persistence.*;


@Entity
public class Book {
    @Id

    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "book_id_generator")
    @SequenceGenerator(name = "book_id_generator",
            sequenceName = "book_id_sequence",
            allocationSize = 1)
    @Column(name = "book_id")
    private int bookId;

    @Column(nullable = false)
    private String title;

    private String author;

    @Column(unique = true, nullable = false)
    private String isbn;

    private String publisher;

    @Column(name = "published_year")
    private int publishedYear;
    private String genre;


    @Column(name = "copies_available", nullable = false)
    private int copiesAvailable;

    public Book() {
    }

    public Book(String title, String author, String isbn, String publisher, int publishedYear, String genre, int copiesAvailable) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.publisher = publisher;
        this.publishedYear = publishedYear;
        this.genre = genre;
        this.copiesAvailable = copiesAvailable;
    }

    public int getBookId() {
        return bookId;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getPublisher() {
        return publisher;
    }

    public int getPublishedYear() {
        return publishedYear;
    }

    public String getGenre() {
        return genre;
    }

    public int getCopiesAvailable() {
        return copiesAvailable;
    }


    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public void setPublishedYear(int publishedYear) {
        this.publishedYear = publishedYear;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setCopiesAvailable(int copiesAvailable) {
        this.copiesAvailable = copiesAvailable;
    }

    @Override
    public String toString() {
        return "Books{" +
                "bookId=" + bookId +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", isbn='" + isbn + '\'' +
                ", publisher='" + publisher + '\'' +
                ", publishedYear=" + publishedYear +
                ", genre='" + genre + '\'' +
                ", copiesAvailable=" + copiesAvailable +
                '}';
    }
}
