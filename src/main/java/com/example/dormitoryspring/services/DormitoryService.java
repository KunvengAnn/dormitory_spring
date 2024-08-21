package com.example.dormitoryspring.services;

import com.example.dormitoryspring.dto.ContractDTO;
import com.example.dormitoryspring.dto.DormitoryDTO;
import com.example.dormitoryspring.dto.RoomDTO;
import com.example.dormitoryspring.dto.response.ContractResponse;
import com.example.dormitoryspring.entity.Contract;
import com.example.dormitoryspring.entity.Dormitory;
import com.example.dormitoryspring.entity.Room;
import com.example.dormitoryspring.repositories.DormitoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DormitoryService {

    @Autowired
    private DormitoryRepository dormitoryRepository;

    public List<DormitoryDTO> getAllDormitory() {
        List<Dormitory> dormitoryList = dormitoryRepository.findAll();
        return dormitoryList.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public DormitoryDTO getDormitoryById(Integer id) {
        return dormitoryRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new RuntimeException("Dormitory not found"));
    }

    // Manual conversion
    private DormitoryDTO convertToDTO(Dormitory dormitory) {
        DormitoryDTO dto = new DormitoryDTO();
        dto.setId_dormitory(dormitory.getId_dormitory());
        dto.setDormitory_name(dormitory.getDormitory_name());

        // Sort rooms by id_room in descending order
        List<RoomDTO> sortedRooms = dormitory.getRooms().stream()
                .sorted(Comparator.comparing(Room::getId_room).reversed())
                .map(this::convertRoomToDTO)
                .collect(Collectors.toList());
        Set<RoomDTO> sortedRoomsSet = new LinkedHashSet<>(sortedRooms);
        dto.setRooms(sortedRoomsSet);

        List<ContractDTO> sortedCtDTO = dormitory.getContracts().stream()
                .sorted(Comparator.comparing(Contract::getId_contract).reversed())
                .map(this::convertContractToDTO)
                .collect(Collectors.toList());
        Set<ContractDTO> sortedCtDTOSt = new LinkedHashSet<>(sortedCtDTO);
        dto.setContracts(sortedCtDTOSt);
        return dto;
    }

    //  conversion for Room and Contract
    private RoomDTO convertRoomToDTO(Room room) {
        RoomDTO dto = new RoomDTO();
        dto.setId_room(room.getId_room());
        dto.setRoom_number(room.getRoom_number());
        dto.setRoom_price(room.getRoom_price());
        dto.setRoom_max_capacity(room.getRoom_max_capacity());
        dto.setQuantity_person(room.getQuantity_person());
        dto.setStatus_is_empty_room(room.getStatus_is_empty_room());
        dto.setStatus_room_is_boy(room.getStatus_room_is_boy());
        return dto;
    }
    private ContractDTO convertContractToDTO(Contract contract) {
        ContractDTO dto = new ContractDTO();
        dto.setId_contract(contract.getId_contract());
        dto.setId_student(contract.getStudent() != null ? contract.getStudent().getId_student() : null);
        dto.setId_dormitory(contract.getDormitory() != null ? contract.getDormitory().getId_dormitory() : null);
        dto.setId_room(contract.getRoom() != null ? contract.getRoom().getId_room() : null);
        dto.setDate_start_contract(contract.getDate_start_contract());
        dto.setDate_end_contract(contract.getDate_end_contract());
        return dto;
    }



    public Dormitory saveDormitory(Dormitory dormitory) {
        return dormitoryRepository.save(dormitory);
    }

    public Dormitory updateDormitory(Integer id, Dormitory dormitory) {
        if (!dormitoryRepository.existsById(id)) {
            throw new RuntimeException("Dormitory not found");
        }
        //dormitory.setId_dormitory(id);
        return dormitoryRepository.save(dormitory);
    }

    public void deleteDormitory(Integer id) {
        if (!dormitoryRepository.existsById(id)) {
            throw new RuntimeException("Dormitory not found");
        }
        dormitoryRepository.deleteById(id);
    }

}
