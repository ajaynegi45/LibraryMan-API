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
	        + "(LOWER(b.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR "
	        + "LOWER(b.author) LIKE LOWER(CONCAT('%', :keyword, '%')) OR "
	        + "LOWER(b.publisher) LIKE LOWER(CONCAT('%', :keyword, '%')) OR "
	        + "LOWER(b.genre) LIKE LOWER(CONCAT('%', :keyword, '%')) OR "
	        + "CAST(b.publishedYear AS string) LIKE %:keyword% OR "
	        + "CAST(b.copiesAvailable AS string) LIKE %:keyword%)")
	Page<Book> searchBook(@Param("keyword") String keyword,Pageable pageable);
}



