package com.example.dormitoryspring.services;

import com.example.dormitoryspring.dto.*;
import com.example.dormitoryspring.dto.request.RoomRequest;
import com.example.dormitoryspring.dto.request.RoomRequest1;
import com.example.dormitoryspring.dto.response.ContractResponse;
import com.example.dormitoryspring.entity.Contract;
import com.example.dormitoryspring.entity.Dormitory;
import com.example.dormitoryspring.entity.Room;
import com.example.dormitoryspring.entity.Student;
import com.example.dormitoryspring.repositories.ContractRepository;
import com.example.dormitoryspring.repositories.DormitoryRepository;
import com.example.dormitoryspring.repositories.RoomRepository;
import com.example.dormitoryspring.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RoomService {
    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private DormitoryRepository dormitoryRepository;

    @Autowired
    private ContractRepository contractRepository;

    private RoomDTO convertRoomToRoomDTO(Room room) {
        RoomDTO roomDTO = new RoomDTO();
        roomDTO.setId_room(room.getId_room());
        roomDTO.setRoom_number(room.getRoom_number());
        roomDTO.setRoom_price(room.getRoom_price());
        roomDTO.setRoom_max_capacity(room.getRoom_max_capacity());
        roomDTO.setQuantity_person(room.getQuantity_person());
        roomDTO.setStatus_is_empty_room(room.getStatus_is_empty_room());
        roomDTO.setStatus_room_is_boy(room.getStatus_room_is_boy());
        roomDTO.setDormitory(convertDormitoryToDormitoryDTO(room.getDormitory()));

        // Sort the contracts by id_contract in descending
        List<ContractDTOTwo> contractDTOs = room.getContracts().stream()
                .sorted(Comparator.comparing(Contract::getId_contract).reversed())
                .map(this::convertContractToContractDTO)
                .collect(Collectors.toList());
        roomDTO.setContracts(contractDTOs);

        return roomDTO;
    }

    private DormitoryDTO convertDormitoryToDormitoryDTO(Dormitory dormitory) {
        DormitoryDTO dormitoryDTO = new DormitoryDTO();
        dormitoryDTO.setId_dormitory(dormitory.getId_dormitory());
        dormitoryDTO.setDormitory_name(dormitory.getDormitory_name());
        return dormitoryDTO;
    }

    private ContractDTOTwo convertContractToContractDTO(Contract ct) {
        ContractDTOTwo contractDTOTwo = new ContractDTOTwo();
        contractDTOTwo.setId_contract(ct.getId_contract());

        // Find the Student by ID and convert to StudentDTO
        String studentId = ct.getStudent().getId_student();
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        StudentDTO st = new StudentDTO();
        st.setId_student(student.getId_student());
        st.setStudent_name(student.getStudent_name());
        st.setStudent_sex(student.getStudent_sex());
        st.setDate_of_birth_student(student.getDate_of_birth_student());
        st.setStudent_class(student.getStudent_class());
        st.setDepartment_of_student(student.getDepartment_of_student());
        st.setStudent_phone(student.getStudent_phone());
        st.setCitizenIdentification(student.getCitizenIdentification());
        st.setNationality(student.getNationality());
        st.setDescriptionAboutSelf(student.getDescriptionAboutSelf());
        st.setStudent_imageUrl(student.getStudent_imageUrl());

        // Set the StudentDTO in ContractDTOTwo
        contractDTOTwo.setStudentDTO(st);

        return contractDTOTwo;
    }


    public List<RoomDTO> getAllRooms() {
        return roomRepository.findAll().stream()
                .map(this::convertRoomToRoomDTO)
                .collect(Collectors.toList());
    }


    public RoomDTO getRoomById(Integer id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        RoomDTO roomDTO = convertRoomToRoomDTO(room);
        return roomDTO;
    }

    //
    public RoomDTO addRoom(RoomRequest1 request) {
        // Convert DormitoryDTO to Dormitory entity
        Dormitory dormitory = dormitoryRepository.findById(request.getId_dormitory())
                .orElseThrow(() -> new RuntimeException("Dormitory not found"));

        Room room = new Room();
        room.setId_room(request.getId_room());
        room.setRoom_number(request.getRoom_number());
        room.setRoom_price(request.getRoom_price());
        room.setRoom_max_capacity(request.getRoom_max_capacity());
        room.setQuantity_person(request.getQuantity_person());
        room.setStatus_is_empty_room(request.getStatus_is_empty_room());
        room.setStatus_room_is_boy(request.getStatus_room_is_boy());
        room.setDormitory(dormitory);

        // Save the room and return the DTO
        Room savedRoom = roomRepository.save(room);
        return convertRoomToRoomDTO(savedRoom);
    }

    public RoomDTO updateRoom(Integer id, RoomRequest1 roomRequest1) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        room.setRoom_number(roomRequest1.getRoom_number());
        room.setRoom_price(roomRequest1.getRoom_price());
        room.setRoom_max_capacity(roomRequest1.getRoom_max_capacity());
        room.setQuantity_person(roomRequest1.getQuantity_person());
        room.setStatus_is_empty_room(roomRequest1.getStatus_is_empty_room());
        room.setStatus_room_is_boy(roomRequest1.getStatus_room_is_boy());

        Dormitory dormitory = dormitoryRepository.findById(roomRequest1.getId_dormitory())
                .orElseThrow(() -> new RuntimeException("Dormitory not found"));
        room.setDormitory(dormitory);

        Room updatedRoom = roomRepository.save(room);
        return convertRoomToRoomDTO(updatedRoom);
    }

    public void deleteRoom(Integer id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        roomRepository.delete(room);
    }
}
