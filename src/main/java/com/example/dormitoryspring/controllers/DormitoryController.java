package com.example.dormitoryspring.controllers;


import com.example.dormitoryspring.dto.DormitoryDTO;
import com.example.dormitoryspring.exception.AppException;
import com.example.dormitoryspring.services.DormitoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/dormitory")
@RequiredArgsConstructor
@Slf4j
public class DormitoryController {
    @Autowired
    private DormitoryService dormitoryService;

    @GetMapping("/getAllDormitory")
    public ResponseEntity<?> getAllDormitory() {
        try{
            List<DormitoryDTO> dormitoryList = dormitoryService.getAllDormitory();
            return new ResponseEntity<>(dormitoryList, HttpStatus.OK);
        }catch (AppException e){
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/getDormitoryById/{id}")
    public ResponseEntity<?> getDormitoryById(@PathVariable Integer id) {
        try {
            DormitoryDTO dormitory = dormitoryService.getDormitoryById(id);
            return new ResponseEntity<>(dormitory, HttpStatus.OK);
        }catch (AppException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
