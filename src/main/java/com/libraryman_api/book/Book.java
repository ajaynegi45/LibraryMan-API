package com.libraryman_api.book;

import jakarta.persistence.*;

/**
 * Represents a book entity in the library management system.
 * This class is mapped to the "books" table in the database.
 */
@Entity
public class Book {
    /**
     * The unique identifier for the book.
     * Automatically generated using a sequence.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "book_id_generator")
    @SequenceGenerator(name = "book_id_generator", sequenceName = "book_id_sequence", allocationSize = 1)
    @Column(name = "book_id")
    private int bookId;

    /**
     * The title of the book.
     * This field cannot be null.
     */
    @Column(nullable = false)
    private String title;

    /**
     * The author of the book.
     */
    private String author;

    /**
     * The ISBN (International Standard Book Number) of the book.
     * This field is unique and cannot be null.
     */
    @Column(unique = true, nullable = false)
    private String isbn;

    /**
     * The publisher of the book.
     */
    private String publisher;

    /**
     * The year the book was published.
     */
    @Column(name = "published_year")
    private int publishedYear;

    /**
     * The genre of the book.
     */
    private String genre;

    /**
     * The number of copies available in the library.
     * This field cannot be null.
     */
    @Column(name = "copies_available", nullable = false)
    private int copiesAvailable;

    /**
     * Default constructor for the Book class.
     */
    public Book() {
    }

    /**
     * Constructs a new Book with the specified attributes.
     *
     * @param title          The title of the book.
     * @param author         The author of the book.
     * @param isbn           The ISBN of the book.
     * @param publisher      The publisher of the book.
     * @param publishedYear  The year the book was published.
     * @param genre          The genre of the book.
     * @param copiesAvailable The number of copies available.
     */
    public Book(String title, String author, String isbn, String publisher, int publishedYear, String genre, int copiesAvailable) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.publisher = publisher;
        this.publishedYear = publishedYear;
        this.genre = genre;
        this.copiesAvailable = copiesAvailable;
    }

    // Getters
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

    // Setters
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

    /**
     * Returns a string representation of the book.
     *
     * @return A string containing the book's details.
     */
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
