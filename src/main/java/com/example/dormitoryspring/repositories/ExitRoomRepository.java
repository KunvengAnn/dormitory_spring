package com.example.dormitoryspring.repositories;

import com.example.dormitoryspring.entity.ExitRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExitRoomRepository extends JpaRepository<ExitRoom, Integer> {

}
