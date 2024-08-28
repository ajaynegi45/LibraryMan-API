package com.libraryman_api.repository;

import com.libraryman_api.entity.Members;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Members, Integer> {

/**
 * SELECT SUM(amount) AS totalFines
 * FROM fines
 * WHERE memberId = [memberId] AND paid = false;
 */
}

