package com.example.dormitoryspring.repositories;

import com.example.dormitoryspring.entity.RoomChange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomChangeRepository extends JpaRepository<RoomChange,Integer> {

}
