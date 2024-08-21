package com.example.dormitoryspring.services;

import com.example.dormitoryspring.entity.Repair;
import com.example.dormitoryspring.repositories.RepairRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RepairService {
    @Autowired
    private RepairRepository repairRepository;

    public List<Repair> getAllRepairs() {
        return repairRepository.findAll();
    }

    public Optional<Repair> getRepairById(Integer id) {
        Optional<Repair> repair = repairRepository.findById(id);
        if(repair.isPresent()) {
            return repair;
        }else{
            throw new RuntimeException("Repair not found");
        }
    }
}
