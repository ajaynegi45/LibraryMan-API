package com.libraryman_api.repository;

import com.libraryman_api.entity.Borrowings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BorrowingRepository extends JpaRepository<Borrowings, Integer> {

    // Underscore (_) used for property traversal, navigating from Borrowings to Members entity via 'member' property
    List<Borrowings> findByMember_memberId(int memberId);
    List<Borrowings> findByBook_bookId(int bookId);
}

