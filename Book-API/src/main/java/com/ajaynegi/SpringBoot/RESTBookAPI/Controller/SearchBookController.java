package com.ajaynegi.SpringBoot.RESTBookAPI.Controller;

import com.ajaynegi.SpringBoot.RESTBookAPI.Entities.BookEntity;
import com.ajaynegi.SpringBoot.RESTBookAPI.Service.SearchBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
public class SearchBookController {

    @Autowired
    private SearchBookService searchBookService;


    @GetMapping("/getAllBooks")
    public List<BookEntity> getAllBooks(){
        List<BookEntity> list = searchBookService.findAllBooks();
        return list;
    }



    // Return the BookEntity representing the book with the given ID
    @GetMapping("/getBookById/{id}")
    public BookEntity getBookById(@PathVariable("id") int id){
        BookEntity bookEntity = searchBookService.findBookById(id);
        return bookEntity;
    }



    // Return a list of BookEntity objects whose bookName start with the given specified prefix.
    @GetMapping("/bookNameStartWith/{bookName}")
    public List<BookEntity> getBookNameStartingWith(@PathVariable("bookName") String bookName){
        List<BookEntity> bookEntity = searchBookService.findByBookNameStartingWith(bookName);
        return bookEntity;
    }



    // Return a list of BookEntity objects whose bookName end with the given specified suffix.
    @GetMapping("/bookNameEndWith/{bookName}")
    public List<BookEntity> getBookNameEndingWith(@PathVariable("bookName") String bookName){
        List<BookEntity> bookEntity = searchBookService.findByBookNameEndingWith(bookName);
        return bookEntity;
    }



    // Return a list of BookEntity objects whose names contain the given specified book name or character.
    @GetMapping("/bookNameContain/{bookName}")
    public List<BookEntity> getBookNameContaining(@PathVariable("bookName") String bookName){
        List<BookEntity> bookEntity = searchBookService.findByBookNameContaining(bookName);
        return bookEntity;
    }



}