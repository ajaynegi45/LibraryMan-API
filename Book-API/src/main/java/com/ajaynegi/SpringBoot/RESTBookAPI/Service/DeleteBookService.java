package com.ajaynegi.SpringBoot.RESTBookAPI.Service;

import com.ajaynegi.SpringBoot.RESTBookAPI.DAO.DeleteBookRepository;
import com.ajaynegi.SpringBoot.RESTBookAPI.Entities.BookEntity;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeleteBookService {

    @Autowired
    private DeleteBookRepository deleteBookRepository;


    // Delete Book From database by given ID
    public boolean deleteById(int id){
        if(deleteBookRepository.existsById(id)){
        deleteBookRepository.deleteById(id);
        return true;
        }
        return false;
    }



    // Deletes all books with the given specified bookName.
    @Transactional
    public void deleteByBookName(String bookName){
        deleteBookRepository.deleteByBookName(bookName);
    }



    // Deletes all books with the given specified authorName.
    @Transactional
    public void deleteAllByAuthorName(String authorName){
        deleteBookRepository.deleteAllByAuthorName(authorName);
    }
}
