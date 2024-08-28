package com.libraryman_api.service;

import com.libraryman_api.entity.Books;
import com.libraryman_api.exception.ResourceNotFoundException;
import com.libraryman_api.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;

    public List<Books> getAllBooks() {
        return bookRepository.findAll();
    }

    public Optional<Books> getBookById(int bookId) {
        return bookRepository.findById(bookId);
    }

    public Books addBook(Books book) {
        return bookRepository.save(book);
    }

    public Books updateBook(int bookId, Books bookDetails) {
        Books book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));
        book.setTitle(bookDetails.getTitle());
        book.setAuthor(bookDetails.getAuthor());
        book.setIsbn(bookDetails.getIsbn());
        book.setPublisher(bookDetails.getPublisher());
        book.setPublishedYear(bookDetails.getPublishedYear());
        book.setGenre(bookDetails.getGenre());
        book.setCopiesAvailable(bookDetails.getCopiesAvailable());
        return bookRepository.save(book);
    }

    public void deleteBook(int bookId) {
        Books book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));
        bookRepository.delete(book);
    }


    public void setBookCopies(int bookId, String operation) {

        Optional<Books> book = getBookById(bookId);
        if (book.isPresent()) {
            if(book.get().getCopiesAvailable() > 0){
                book.get().setCopiesAvailable(book.get().getCopiesAvailable() - 1);
            }
        }
    }


}

