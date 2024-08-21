package com.example.dormitoryspring.services;

import com.example.dormitoryspring.entity.RoomChange;
import com.example.dormitoryspring.repositories.RoomChangeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoomChangeService {
    @Autowired
    private RoomChangeRepository roomChangeRepository;

    public List<RoomChange> getAllRooms(){
        return roomChangeRepository.findAll();
    }
    public Optional<RoomChange> getRoomById(Integer id){
        Optional<RoomChange> roomChange = roomChangeRepository.findById(id);
        if(roomChange.isPresent()){
            return roomChange;
        }else{
            throw new RuntimeException("room change not found");
        }
    }
}
