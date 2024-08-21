package com.example.dormitoryspring.services;

import com.example.dormitoryspring.entity.Equipment;
import com.example.dormitoryspring.repositories.EquipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EquipmentService {
    @Autowired
    private EquipmentRepository equipmentRepository;

    public List<Equipment> getAllEquipment() {
        return equipmentRepository.findAll();
    }

    public Optional<Equipment> getEquipmentById(Integer id) {
        Optional<Equipment> equipment = equipmentRepository.findById(id);
        if(equipment.isPresent()) {
            return equipment;
        }else{
            throw new RuntimeException("No equipment found with id " + id);
        }
    }
}
