package com.ajaynegi.SpringBoot.RESTBookAPI.DAO;

import com.ajaynegi.SpringBoot.RESTBookAPI.Entities.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


/**
 * For more Query Method
 * <p>
 * <a href="https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.query-creation">Visit Spring Data JPA Documentation</a>
 * **/
public interface SearchBookRepository extends JpaRepository<BookEntity,Integer> {


    // Return the BookEntity representing the book with the given ID
    public BookEntity findById(int id);


    // Return a list of BookEntity objects whose bookName start with the given specified prefix.
    public List<BookEntity> findByBookNameStartingWith(String prefix);


    // Return a list of BookEntity objects whose bookName end with the given specified suffix.
    public List<BookEntity> findByBookNameEndingWith(String suffix);


    // Return a list of BookEntity objects whose names contain the given specified book name or character.
    public List<BookEntity> findByBookNameContaining(String bookName);
}
