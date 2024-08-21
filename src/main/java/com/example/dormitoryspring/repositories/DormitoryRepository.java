package com.example.dormitoryspring.repositories;

import com.example.dormitoryspring.entity.Dormitory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DormitoryRepository extends JpaRepository<Dormitory, Integer> {
}

