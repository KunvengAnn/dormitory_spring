package com.example.dormitoryspring.controllers;

import com.example.dormitoryspring.dto.RoomDTO;
import com.example.dormitoryspring.dto.request.RoomRequest;
import com.example.dormitoryspring.dto.request.RoomRequest1;
import com.example.dormitoryspring.exception.AppException;
import com.example.dormitoryspring.services.RoomService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rooms")
@Slf4j
public class RoomController {
    @Autowired
    private RoomService roomService;

    @GetMapping("/getAllRooms")
    public ResponseEntity<?> getAllRooms() {
        try {
            List<RoomDTO> roomDto = roomService.getAllRooms();
            return ResponseEntity.ok(roomDto);
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRoomById(@PathVariable Integer id){
        try {
            RoomDTO roomDTO = roomService.getRoomById(id);
            return ResponseEntity.ok(roomDTO);
        }catch (AppException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    //
    @PostMapping
    public ResponseEntity<?> addRoom(@RequestBody RoomRequest1 roomRequest) {
        try {
            RoomDTO roomDTO = roomService.addRoom(roomRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(roomDTO);
        } catch (AppException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRoom(@PathVariable Integer id, @RequestBody RoomRequest1 roomRequest1) {
        try {
            RoomDTO roomDTO = roomService.updateRoom(id, roomRequest1);
            return ResponseEntity.ok(roomDTO);
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRoom(@PathVariable Integer id) {
        try {
            roomService.deleteRoom(id);
            return ResponseEntity.noContent().build();
        } catch (AppException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
