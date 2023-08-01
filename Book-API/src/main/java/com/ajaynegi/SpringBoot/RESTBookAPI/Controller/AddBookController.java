package com.ajaynegi.SpringBoot.RESTBookAPI.Controller;

import com.ajaynegi.SpringBoot.RESTBookAPI.Entities.BookEntity;
import com.ajaynegi.SpringBoot.RESTBookAPI.Service.AddBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AddBookController {

    @Autowired
    private AddBookService addBookService;

    @PostMapping("/addBook")
    public ResponseEntity<Void> addBook(@RequestBody BookEntity bookEntity){
        try{
             boolean value =addBookService.addBook(bookEntity);
             if(value){
                 return ResponseEntity.status(HttpStatus.CREATED).build();
             }
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}