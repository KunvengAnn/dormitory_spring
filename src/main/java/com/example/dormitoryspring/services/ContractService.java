package com.example.dormitoryspring.services;

import com.example.dormitoryspring.dto.request.ContractRequest;
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

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContractService {

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private DormitoryRepository dormitoryRepository;

    @Autowired
    private RoomRepository roomRepository;

    public List<ContractResponse> getAllContracts() {
        return contractRepository.findAll().stream()
                .map(this::convertToContractResponse)
                .collect(Collectors.toList());
    }

    private ContractResponse convertToContractResponse(Contract contract) {
        ContractResponse response = new ContractResponse();
        response.setId_contract(contract.getId_contract());
        response.setId_student(contract.getStudent().getId_student()); // Adjust if needed
        response.setId_dormitory(contract.getDormitory().getId_dormitory());
        response.setId_room(contract.getRoom().getId_room());
        response.setDate_start_contract(contract.getDate_start_contract());
        response.setDate_end_contract(contract.getDate_end_contract());
        return response;
    }

    public ContractResponse getContractById(Integer id) {
        Contract contract = contractRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contract not found with id: " + id));
        return convertToContractResponse(contract);
    }

    public ContractResponse addContract(ContractRequest request) {
        Student student = studentRepository.findById(request.getId_student())
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + request.getId_student()));

        Dormitory dormitory = dormitoryRepository.findById(request.getId_dormitory())
                .orElseThrow(() -> new RuntimeException("Dormitory not found with id: " + request.getId_dormitory()));

        Room room = roomRepository.findById(request.getId_room())
                .orElseThrow(() -> new RuntimeException("Room not found with id: " + request.getId_room()));

        Contract contract = new Contract();
        contract.setId_contract(request.getId_contract());
        contract.setStudent(student);
        contract.setDormitory(dormitory);
        contract.setRoom(room);
        contract.setDate_start_contract(request.getDate_start_contract());
        contract.setDate_end_contract(request.getDate_end_contract());

        // Save the contract
        Contract savedContract = contractRepository.save(contract);
        return convertToContractResponse(savedContract);
    }

    public ContractResponse updateContract(Integer id, ContractRequest request) {
        Contract contract = contractRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contract not found with id: " + id));

        Student student = studentRepository.findById(request.getId_student())
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + request.getId_student()));

        Dormitory dormitory = dormitoryRepository.findById(request.getId_dormitory())
                .orElseThrow(() -> new RuntimeException("Dormitory not found with id: " + request.getId_dormitory()));

        Room room = roomRepository.findById(request.getId_room())
                .orElseThrow(() -> new RuntimeException("Room not found with id: " + request.getId_room()));

        contract.setStudent(student);
        contract.setDormitory(dormitory);
        contract.setRoom(room);
        contract.setDate_start_contract(request.getDate_start_contract());
        contract.setDate_end_contract(request.getDate_end_contract());

        Contract updatedContract = contractRepository.save(contract);
        return convertToContractResponse(updatedContract);
    }

    public void deleteContract(Integer id) {
        Contract contract = contractRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contract not found with id: " + id));
        contractRepository.delete(contract);
    }
}
