package com.ajaynegi.SpringBoot.RESTBookAPI.Controller;

import com.ajaynegi.SpringBoot.RESTBookAPI.Entities.BookEntity;
import com.ajaynegi.SpringBoot.RESTBookAPI.Service.UpdateBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UpdateBookController {

    @Autowired
    private UpdateBookService updateBookService;




    // Receive a BookEntity object as a request body containing updated book details and extract the bookId from the URL path.
    @PutMapping("/updatebook/{bookid}")
    public ResponseEntity<BookEntity> updateBookEntity(@RequestBody BookEntity book, @PathVariable("bookid") int bookId){

        // Call the updateBook method to update the book details. and the method returns a boolean indicating whether the update was successful or not.
        boolean value = this.updateBookService.updateBook(book,bookId);

        // If the update was successful (the book exists and was updated), return a response with status 202 (ACCEPTED).
        if(value){
            return ResponseEntity.status(HttpStatus.ACCEPTED).build();
        }

        // If the update was not successful (the book with the given ID was not found), return a response with status 404 (NOT_FOUND).
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

}