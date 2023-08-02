package com.ajaynegi.SpringBoot.RESTBookAPI.Controller;

import com.ajaynegi.SpringBoot.RESTBookAPI.Service.DeleteBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DeleteBookController {

    @Autowired
    private DeleteBookService deleteBookService;



    // Delete Book From database by given ID
    @DeleteMapping("/deleteBookById/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable("id") int id){
        try{
            boolean value = deleteBookService.deleteById(id);
            if(value){
                return ResponseEntity.status(HttpStatus.ACCEPTED).build();
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }




    // Deletes all books with the given specified bookName.
    @DeleteMapping("/deleteBookByName/{bookName}")
    public ResponseEntity<Void> deleteByBookName(@PathVariable("bookName") String bookName){
        try{
            deleteBookService.deleteByBookName(bookName);
            return ResponseEntity.status(HttpStatus.ACCEPTED).build();
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }



    // Deletes all books with the given specified authorName.
    @DeleteMapping("/deleteAllByAuthor/{authorName}")
    public ResponseEntity<Void> deleteAllByAuthor(@PathVariable("authorName") String authorName){
        try{
            deleteBookService.deleteAllByAuthorName(authorName);
            return ResponseEntity.status(HttpStatus.ACCEPTED).build();
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


}