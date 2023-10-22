package com.ajaynegi.SpringBoot.RESTBookAPI.Controller;

import com.ajaynegi.SpringBoot.RESTBookAPI.Entities.BookEntity;
import com.ajaynegi.SpringBoot.RESTBookAPI.ExceptionHandler.BookNotFoundException;
import com.ajaynegi.SpringBoot.RESTBookAPI.Service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BookController
{
    @Autowired
    private BookService bookService;

    //    Add book
    @PostMapping("/addBook")
    public ResponseEntity<Void> addBook(@RequestBody BookEntity bookEntity)
    {
        try{
            boolean value =this.bookService.addBook(bookEntity);
            if(value){
                return ResponseEntity.status(HttpStatus.CREATED).build();
            }
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    // Delete Book From database by given ID
    @DeleteMapping("/deleteBookById/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable int id) throws BookNotFoundException {

            boolean value = this.bookService.deleteById(id);
            if(value){
                return ResponseEntity.status(HttpStatus.ACCEPTED).build();
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

    }


    // Deletes all books with the given specified bookName.
    @DeleteMapping("/deleteBookByName/{bookName}")
    public ResponseEntity<Void> deleteByBookName(@PathVariable String bookName){
        try{
            this.bookService.deleteByBookName(bookName);
            return ResponseEntity.status(HttpStatus.ACCEPTED).build();
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }



    // Deletes all books with the given specified authorName.
    @DeleteMapping("/deleteAllByAuthor/{authorName}")
    public ResponseEntity<Void> deleteAllByAuthor(@PathVariable String authorName){
        try{
            this.bookService.deleteAllByAuthorName(authorName);
            return ResponseEntity.status(HttpStatus.ACCEPTED).build();
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // Fetch all books from Database
    @GetMapping("/getAllBooks")
    public List<BookEntity> getAllBooks(){
        return this.bookService.findAllBooks();
    }


    // Return the BookEntity representing the book with the given ID
    @GetMapping("/getBookById/{id}")
    public BookEntity getBookById(@PathVariable int id) throws BookNotFoundException {
        return this.bookService.findBookById(id);
    }



    // Return a list of BookEntity objects whose bookName start with the given specified prefix.
    @GetMapping("/bookNameStartWith/{bookName}")
    public List<BookEntity> getBookNameStartingWith(@PathVariable String bookName){
        return this.bookService.findByBookNameStartingWith(bookName);
    }



    // Return a list of BookEntity objects whose bookName end with the given specified suffix.
    @GetMapping("/bookNameEndWith/{bookName}")
    public List<BookEntity> getBookNameEndingWith(@PathVariable String bookName){
        return this.bookService.findByBookNameEndingWith(bookName);
    }



    // Return a list of BookEntity objects whose names contain the given specified book name or character.
    @GetMapping("/bookNameContain/{bookName}")
    public List<BookEntity> getBookNameContaining(@PathVariable String bookName){
        return this.bookService.findByBookNameContaining(bookName);
    }


    // Receive a BookEntity object as a request body containing updated book details and extract the bookId from the URL path.
    @PutMapping("/updatebook/{bookId}")
    public ResponseEntity<BookEntity> updateBookEntity(@RequestBody BookEntity book, @PathVariable int bookId) throws BookNotFoundException {

        // Call the updateBook method to update the book details. and the method returns a boolean indicating whether the update was successful or not.
        boolean value = this.bookService.updateBook(book,bookId);

        // If the update was successful (the book exists and was updated), return a response with status 202 (ACCEPTED).
        if(value){
            return ResponseEntity.status(HttpStatus.ACCEPTED).build();
        }

        // If the update was not successful (the book with the given ID was not found), return a response with status 404 (NOT_FOUND).
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

}
