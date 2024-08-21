package com.example.dormitoryspring.repositories;

import com.example.dormitoryspring.entity.Water_electricity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Water_electricityRepository extends JpaRepository<Water_electricity, Integer> {

}