package com.libraryman_api.book;

import jakarta.validation.constraints.*;

public class BookDto {


    private int bookId;
    @NotBlank(message = "Title is required ")
    @Size(min = 1, max = 255, message = "Title must be between 1 and 255 characters")
    private String title;

    @NotBlank(message = "Author is required")
    @Size(min = 1, max = 100, message = "Author name must be between 1 and 100 characters")
    private String author;

    @NotBlank(message = "isbn is required")
    @Pattern(regexp = "^(978|979)-\\d{10}$", message = "Invalid ISBN format. Format must be '978-XXXXXXXXXX' or '979-XXXXXXXXXX'")
    private String isbn;

    @NotBlank(message = "Publisher is required")
    @Size(min = 1, max = 100, message = "Publisher name must be between 1 and 100 characters")
    private String publisher;

    @Min(value = 1500, message = "Published year must be at least 1500")
    @Max(value = 2100, message = "Published year cannot be more than 2100")
    private int publishedYear;

    @NotBlank(message = "Genre is required")
    @Size(min = 1, max = 50, message = "Genre must be between 1 and 50 characters")
    private String genre;

    @Min(value = 0, message = "Copies available cannot be negative")
    private int copiesAvailable;

    public BookDto(int bookId, String title, String author, String isbn, String publisher, int publishedYear, String genre, int copiesAvailable) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.publisher = publisher;
        this.publishedYear = publishedYear;
        this.genre = genre;
        this.copiesAvailable = copiesAvailable;
    }

    public BookDto() {
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public int getPublishedYear() {
        return publishedYear;
    }

    public void setPublishedYear(int publishedYear) {
        this.publishedYear = publishedYear;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getCopiesAvailable() {
        return copiesAvailable;
    }

    public void setCopiesAvailable(int copiesAvailable) {
        this.copiesAvailable = copiesAvailable;
    }

    @Override
    public String toString() {
        return "BookDto{" +
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
