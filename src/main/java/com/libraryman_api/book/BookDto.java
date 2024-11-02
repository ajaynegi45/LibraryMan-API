package com.libraryman_api.book;

public class BookDto {
    private int bookId;
    private String title;
    private String author;
    private String isbn;
    private String publisher;
    private int publishedYear;
    private String genre;
    private int copiesAvailable;
    private String keyword;  // Define the keyword variable

    public BookDto(int bookId, String title, String author, String isbn, String publisher, int publishedYear, String genre, int copiesAvailable, String keyword) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.publisher = publisher;
        this.publishedYear = publishedYear;
        this.genre = genre;
        this.copiesAvailable = copiesAvailable;
        this.keyword = keyword;
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

    public String getKeyword() {  // Changed method name to camelCase and corrected return type
        return keyword;
    }

    public void setKeyword(String keyword) {  // Added a setter for keyword
        this.keyword = keyword;
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
                ", keyword='" + keyword + '\'' +
                '}';
    }
}
