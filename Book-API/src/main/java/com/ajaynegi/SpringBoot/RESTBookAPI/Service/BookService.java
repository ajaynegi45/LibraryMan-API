package com.ajaynegi.SpringBoot.RESTBookAPI.Service;

import com.ajaynegi.SpringBoot.RESTBookAPI.DAO.BookRepository;
import com.ajaynegi.SpringBoot.RESTBookAPI.Entities.BookEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class BookService {


    @Autowired
    private BookRepository bookRepository;


    //Todo: Getting All Books
     public List<BookEntity> getAllBook(){
         List<BookEntity> list = (List<BookEntity>) this.bookRepository.findAll();
         return list;
     }


    //TODO: Getting Book By ID
    public BookEntity getBookById(int id){
         BookEntity bookEntity = null;
         try {
             bookEntity = this.bookRepository.findById(id);
         }catch (Exception e){
             System.out.println("\n\nError Enconter in getBookById Book Service");
             e.printStackTrace();
             System.out.println("\n\n");
         }
        return bookEntity;
    }


    //TODO: Add Book to database
    public BookEntity addBook(BookEntity book){
         BookEntity bookEntity = bookRepository.save(book);
         return bookEntity;
    }


    //TODO: Delete Book From database
    public void deleteBook(int id){
         bookRepository.deleteById(id);
        System.out.println("\n\nSuccessfull Deleted Book Id "+ id+"\n\n");
    }


    //TODO: Update Book Details to Database
    public void updateBook(BookEntity book,int bookId){
         book.setId(bookId);
         bookRepository.save(book);
    }


}