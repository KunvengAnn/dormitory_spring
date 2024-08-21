package com.example.dormitoryspring.repositories;

import com.example.dormitoryspring.entity.RoomEquipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomEquipmentRepository extends JpaRepository<RoomEquipment,Integer> {

}
