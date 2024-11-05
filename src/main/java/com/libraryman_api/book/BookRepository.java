package com.libraryman_api.book;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
	
	/**
	 * This method use SQL Query for finding book based on
	 * title, author, genre, publishedYear, etc. By using LIKE operator
	 * it search from database based on keyword entered.
	 * 
	 * @param keyword
	 * @param pageable
	 * @return
	 */
	
	@Query("SELECT b FROM Book b WHERE "
			+ "b.title LIKE %:keyword% OR "
			+ "b.author LIKE %:keyword% OR "
			+ "b.publisher LIKE %:keyword% OR "
			+ "b.genre LIKE %:keyword% OR "
			+ "CAST(b.publishedYear As string) LIKE %:keyword% OR "
			+ "CAST(b.copiesAvailable As string) LIKE %:keyword%")
	Page<Book> searchBook(@Param("keyword") String keyword,Pageable pageable);
}



