package com.ajaynegi.SpringBoot.RESTBookAPI.Service;

import com.ajaynegi.SpringBoot.RESTBookAPI.DAO.BookRepository;
import com.ajaynegi.SpringBoot.RESTBookAPI.Entities.BookEntity;
import com.ajaynegi.SpringBoot.RESTBookAPI.ExceptionHandler.BookNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService
{
    @Autowired
    private BookRepository bookRepository;

    // Logger to print the logs
    Logger logger=LoggerFactory.getLogger(BookService.class);

    // Add New Book to Database
    public boolean addBook(BookEntity book)
    {
        logger.info("Entry into the addBook method");
        try
        {
            BookEntity bookEntity = this.bookRepository.save(book);
            logger.info("Book Added successfully, exit from addBook method");
            return true;
        }
        catch (Exception exception)
        {
            logger.error("Unable to add book : {}",exception.getMessage());
        }

        return false;
    }

    // Delete Book From database by given ID
    public boolean deleteById(int id) throws BookNotFoundException
    {
        logger.info("Entry into the deleteById method ID : {}",id);
        BookEntity book=this.bookRepository.findById(id).orElseThrow(()->new BookNotFoundException("Book with ID : "+id+" Not FOUND"));
        this.bookRepository.delete(book);
        logger.info("Book Deleted successfully, exit from the deleteById method");
        return true;
    }



    // Deletes all books with the given specified bookName.
    @Transactional
    public void deleteByBookName(String bookName)
    {
        logger.info("Entry into the deleteByBookName method name : {}",bookName);
        this.bookRepository.deleteByBookName(bookName);
        logger.info("Exit from the deleteByBookName method");
    }



    // Deletes all books with the given specified authorName.
    @Transactional
    public void deleteAllByAuthorName(String authorName)
    {
        logger.info("Entry into the deleteByBookName method name : {}",authorName);
        this.bookRepository.deleteAllByAuthorName(authorName);
        logger.info("Exit from the deleteByBookName method");
    }

    // Return a list of all BookEntity objects are present in Database
    public List<BookEntity> findAllBooks()
    {
        logger.info("Entry into the findAllBooks method");
        List<BookEntity> list =this.bookRepository.findAll();
        logger.info("Exit from the findAllBooks method");
        return list;
    }

    // Getting Book By ID
    public BookEntity findBookById(int id) throws BookNotFoundException {
        logger.info("Entry into the findBookById method ID : {}",id);
        BookEntity book=this.bookRepository.findById(id).orElseThrow(()->new BookNotFoundException("Book with ID : "+id+" Not FOUND"));
        logger.info("Exit from the findBookById method");
        return book;
    }

    // Return a list of BookEntity objects whose bookName start with the given specified prefix.
    public List<BookEntity> findByBookNameStartingWith(String prefix)
    {
        logger.info("Entry into the findByBookNameStartingWith method prefix : {}",prefix);
        List<BookEntity> books = this.bookRepository.findByBookNameStartingWith(prefix);
        logger.info("Exit from the findByBookNameStartingWith method");
        return books;
    }

    // Return a list of BookEntity objects whose bookName end with the given specified suffix.
    public List<BookEntity> findByBookNameEndingWith(String suffix)
    {
        logger.info("Entry into the findByBookNameEndingWith method suffix : {}",suffix);
        List<BookEntity> books = this.bookRepository.findByBookNameEndingWith(suffix);
        logger.info("Exit from the findByBookNameEndingWith method");
        return books;
    }

    // Return a list of BookEntity objects whose names contain the given specified book name or character.
    public List<BookEntity> findByBookNameContaining(String search)
    {
        logger.info("Entry into the findByBookNameContaining method search : {}",search);
        List<BookEntity> books = this.bookRepository.findByBookNameContaining(search);
        logger.info("Exit from the findByBookNameContaining method");
        return books;
    }

    public boolean updateBook(BookEntity updatedBook, int bookId) throws BookNotFoundException {

        // Retrieve the BookEntity object with the given bookId from the database.
        logger.info("Entry into the updateBook method ID : {}",bookId);
        BookEntity book=this.bookRepository.findById(bookId).orElseThrow(()->new BookNotFoundException("Book with ID : "+bookId+" Not FOUND"));

        // Save the updated BookEntity object to the database using the UpdateBookRepository.
        updatedBook.setId(bookId);
        this.bookRepository.save(updatedBook);
        logger.info("Book updated successfully, exit from the updateBook method");
        return true;

    }

}
