package com.ajaynegi.SpringBoot.RESTBookAPI.DAO;

import com.ajaynegi.SpringBoot.RESTBookAPI.Entities.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeleteBookRepository extends JpaRepository<BookEntity,Integer>  {



    // Deletes the book with the given ID
    public void deleteById(int id);



    // Deletes all books with the given specified bookName.
    public void deleteByBookName(String bookName);



    // Deletes all books with the given specified authorName.
    public void deleteAllByAuthorName(String authorName);

}


