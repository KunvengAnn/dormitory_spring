package com.example.dormitoryspring.services;

import com.example.dormitoryspring.entity.Water_electricity;
import com.example.dormitoryspring.repositories.Water_electricityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class Water_electricityService {

    @Autowired
    private Water_electricityRepository water_electricityRepository;

    public List<Water_electricity> getAllWater_electricity() {
        return water_electricityRepository.findAll();
    }

    public Optional<Water_electricity> getWater_electricityById(Integer id) {
        Optional<Water_electricity> water_electricity = water_electricityRepository.findById(id);
        if(water_electricity.isPresent()) {
            return water_electricity;
        }else{
            throw new RuntimeException("Water electricity not found");
        }
    }
}
