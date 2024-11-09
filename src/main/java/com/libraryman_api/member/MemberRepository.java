package com.libraryman_api.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Members, Integer> {

    Optional<Members> findByMemberId(int memberId);

    Optional<Members> findByUsername(String username);

    @Query(value = "SELECT m.name AS memberName, COUNT(b.borrowing_id) AS borrowCount, " +
            "SUM(CASE WHEN b.return_date IS NULL THEN 1 ELSE 0 END) AS currentBorrowings " +
            "FROM members m LEFT JOIN borrowings b ON m.member_id = b.member_id " +
            "GROUP BY m.member_id ORDER BY borrowCount DESC", nativeQuery = true)
    List<Map<String, Object>> getMemberActivityReport();
}