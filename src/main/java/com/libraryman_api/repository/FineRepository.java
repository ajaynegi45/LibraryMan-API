package com.libraryman_api.repository;

import com.libraryman_api.entity.Fines;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FineRepository extends JpaRepository<Fines, Integer> {
}


