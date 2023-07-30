package com.ajaynegi.SpringBoot.RESTBookAPI.Controller;

import com.ajaynegi.SpringBoot.RESTBookAPI.Entities.BookEntity;
import com.ajaynegi.SpringBoot.RESTBookAPI.Service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
public class BookController {

    @Autowired
    private BookService bookService;


    @GetMapping("/book")
    public BookEntity getBookEntity(){
        BookEntity book = new BookEntity();
        book.setId(1);
        book.setBookName("Science BookEntity");
        book.setAuthorName("H.C Verma");

        // Spring Boot Jackson dependency automatically convert Object into JSON Data
        return book;
    }


    // TODO: Getting All Book
    @GetMapping("/getbooks")
    public ResponseEntity<List<BookEntity>> getBookEntitys(){
       List<BookEntity> list = bookService.getAllBook();
       if(list.isEmpty()){
           return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
       }
       return ResponseEntity.status(HttpStatus.OK).body(list);
    }


    // TODO: Getting single Book by Id
    @GetMapping("/book/{id}")
    public BookEntity getBookEntity(@PathVariable("id") int id){
        return bookService.getBookById(id);
    }


    // TODO: Add Single Book
    @PostMapping("/postbook")
    public ResponseEntity<BookEntity> addBookEntity(@RequestBody BookEntity book){
        BookEntity b = null;
        try {
            b = this.bookService.addBook(book);
            return ResponseEntity.of(Optional.of(b));
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    // TODO: Delete Single Book by Id
    @DeleteMapping("/deletebook/{id}")
    public ResponseEntity<Void> deletebook(@PathVariable("id") int id){
        try {
            this.bookService.deleteBook(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }


    // TODO: Update Book Details
    @PutMapping("/updatebook/{bookid}")
    public BookEntity updateBookEntity( @RequestBody BookEntity book, @PathVariable ("bookid") int bookId){
        this.bookService.updateBook(book,bookId);
        return book;
    }
}