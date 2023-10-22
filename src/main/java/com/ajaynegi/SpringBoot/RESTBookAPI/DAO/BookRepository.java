package com.ajaynegi.SpringBoot.RESTBookAPI.DAO;

import com.ajaynegi.SpringBoot.RESTBookAPI.Entities.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<BookEntity,Integer>
{
    // Deletes all books with the given specified bookName.
    public void deleteByBookName(String bookName);

    // Deletes all books with the given specified authorName.
    public void deleteAllByAuthorName(String authorName);

    // Return a list of BookEntity objects whose bookName start with the given specified prefix.
    public List<BookEntity> findByBookNameStartingWith(String prefix);

    // Return a list of BookEntity objects whose bookName end with the given specified suffix.
    public List<BookEntity> findByBookNameEndingWith(String suffix);

    // Return a list of BookEntity objects whose names contain the given specified book name or character.
    public List<BookEntity> findByBookNameContaining(String bookName);
}
