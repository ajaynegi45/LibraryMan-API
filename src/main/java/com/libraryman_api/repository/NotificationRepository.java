package com.libraryman_api.repository;

import com.libraryman_api.entity.Notifications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notifications, Integer> {
    List<Notifications> findByMember_memberId(int memberId);
}
