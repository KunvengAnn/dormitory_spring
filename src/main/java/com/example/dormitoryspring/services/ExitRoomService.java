package com.example.dormitoryspring.services;

import com.example.dormitoryspring.entity.ExitRoom;
import com.example.dormitoryspring.repositories.ExitRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExitRoomService {
    @Autowired
    private ExitRoomRepository exitRoomRepository;

    public List<ExitRoom> getAllExitRooms() {
        return exitRoomRepository.findAll();
    }

    public Optional<ExitRoom> getExitRoomById(Integer id) {
        Optional<ExitRoom> exitRoom = exitRoomRepository.findById(id);
        if(exitRoom.isPresent()) {
            return exitRoom;
        }else{
         throw new RuntimeException("ExitRoom not found!");
        }
    }
}
