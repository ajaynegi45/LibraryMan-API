package com.ajaynegi.SpringBoot.RESTBookAPI.Service;

import com.ajaynegi.SpringBoot.RESTBookAPI.DAO.UpdateBookRepository;
import com.ajaynegi.SpringBoot.RESTBookAPI.Entities.BookEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UpdateBookService {

    @Autowired
    private UpdateBookRepository updateBookRepository;


    // Update Book Details to Database
    public boolean updateBook(BookEntity book, int bookId){

        // Retrieve the BookEntity object with the given bookId from the database.
        Optional<BookEntity> bookEntity = this.updateBookRepository.findById(bookId);

        // Check if the book with the specified bookId exists in the database.
        if(bookEntity.isPresent()){
            book.setId(bookId);
            // Save the updated BookEntity object to the database using the UpdateBookRepository.
            updateBookRepository.save(book);
            return true;
        }

        // If the book with the given bookId was not found in the database, return false.
        return false;
    }

}
