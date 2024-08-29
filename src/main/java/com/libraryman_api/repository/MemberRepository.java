package com.libraryman_api.repository;

import com.libraryman_api.entity.Borrowings;
import com.libraryman_api.entity.Members;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Members, Integer> {

    Optional<Members> findByMemberId(int memberId);


/**
 * SELECT SUM(amount) AS totalFines
 * FROM fines
 * WHERE memberId = [memberId] AND paid = false;
 */
}

