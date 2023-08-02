package com.ajaynegi.SpringBoot.RESTBookAPI.Service;

import com.ajaynegi.SpringBoot.RESTBookAPI.DAO.AddBookRepository;
import com.ajaynegi.SpringBoot.RESTBookAPI.Entities.BookEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddBookService {

    @Autowired
    private AddBookRepository addBookRepository;


    // Add New Book to Database
    public boolean addBook(BookEntity book){
        BookEntity bookEntity = addBookRepository.save(book);

        if (addBookRepository.existsById(bookEntity.getId())) {
            return true;
        }
        return false;
    }
}
