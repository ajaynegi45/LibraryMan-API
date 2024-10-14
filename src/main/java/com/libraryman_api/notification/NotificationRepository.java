package com.libraryman_api.notification;

import com.libraryman_api.borrowing.Borrowings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notifications, Integer> {
    List<Notifications> findByMember_memberId(int memberId);

    @Query("SELECT b FROM Borrowings b WHERE b.dueDate BETWEEN :today AND :twoDaysFromNow")
    List<Borrowings> findBorrowingsDueInDays(@Param("today") Date today, @Param("twoDaysFromNow") Date twoDaysFromNow);
}

