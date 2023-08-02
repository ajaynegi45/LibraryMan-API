package com.ajaynegi.SpringBoot.RESTBookAPI.DAO;

import com.ajaynegi.SpringBoot.RESTBookAPI.Entities.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UpdateBookRepository extends JpaRepository<BookEntity,Integer> {
}
