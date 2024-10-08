package com.libraryman_api.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Members, Integer> {

    Optional<Members> findByMemberId(int memberId);

    Optional<Members> findByUsername(String username);

/**
 * SELECT SUM(amount) AS totalFines
 * FROM fines
 * WHERE memberId = [memberId] AND paid = false;
 */
}

