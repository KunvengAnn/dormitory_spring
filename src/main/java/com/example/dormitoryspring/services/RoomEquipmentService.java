package com.example.dormitoryspring.services;

import com.example.dormitoryspring.entity.RoomEquipment;
import com.example.dormitoryspring.repositories.RoomEquipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoomEquipmentService {
    @Autowired
    private RoomEquipmentRepository roomEquipmentRepository;

    public List<RoomEquipment> findAll() {
        return roomEquipmentRepository.findAll();
    }

    public Optional<RoomEquipment> findById(Integer id) {
        Optional<RoomEquipment> roomEquipment = roomEquipmentRepository.findById(id);
        if(roomEquipment.isPresent()) {
            return roomEquipment;
        }else{
            throw new RuntimeException("Room equipment not found");
        }
    }
}
