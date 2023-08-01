package com.ajaynegi.SpringBoot.RESTBookAPI.Service;


import com.ajaynegi.SpringBoot.RESTBookAPI.DAO.SearchBookRepository;
import com.ajaynegi.SpringBoot.RESTBookAPI.Entities.BookEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SearchBookService {

    @Autowired
    private SearchBookRepository searchBookRepository;



    // Return a list of all BookEntity objects are present in Database
    public List<BookEntity> findAllBooks(){
        List<BookEntity> list = (List<BookEntity>) this.searchBookRepository.findAll();
        return list;
    }



    // Getting Book By ID
    public BookEntity findBookById(int id){
        BookEntity bookEntity = null;
        try {
            bookEntity = this.searchBookRepository.findById(id);
        }catch (Exception e){
            System.out.println("\n\nError Enconter in getBookById Book Service");
            e.printStackTrace();
            System.out.println("\n\n");
        }
        return bookEntity;
    }



    // Return the BookEntity representing the book with the given ID
    public BookEntity findById(int id){
        BookEntity bookEntity = this.searchBookRepository.findById(id);
        return bookEntity;
    }



    // Return a list of BookEntity objects whose bookName start with the given specified prefix.
    public List<BookEntity> findByBookNameStartingWith(String prefix){
        List<BookEntity> list = this.searchBookRepository.findByBookNameStartingWith(prefix);
        return list;
    }




    // Return a list of BookEntity objects whose bookName end with the given specified suffix.
    public List<BookEntity> findByBookNameEndingWith(String suffix){
        List<BookEntity> list = this.searchBookRepository.findByBookNameEndingWith(suffix);
        return list;
    }



    // Return a list of BookEntity objects whose names contain the given specified book name or character.
    public List<BookEntity> findByBookNameContaining(String bookName){
        System.out.println("Service: Searching for books with name containing: " + bookName);
        List<BookEntity> list = this.searchBookRepository.findByBookNameContaining(bookName);
        System.out.println("Service: Found " + list.size() + " books.");
        return list;
    }

}