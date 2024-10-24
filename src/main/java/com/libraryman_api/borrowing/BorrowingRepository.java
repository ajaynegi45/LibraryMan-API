package com.libraryman_api.borrowing;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BorrowingRepository extends JpaRepository<Borrowings, Integer> {

    // Underscore (_) used for property traversal, navigating from Borrowings to Members entity via 'member' property
    Page<Borrowings> findByMember_memberId(int memberId, Pageable pageable);

    List<Borrowings> findByBook_bookId(int bookId);
}

