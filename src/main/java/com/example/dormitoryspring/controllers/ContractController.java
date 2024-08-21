package com.example.dormitoryspring.controllers;

import com.example.dormitoryspring.dto.request.ContractRequest;
import com.example.dormitoryspring.dto.response.ContractResponse;
import com.example.dormitoryspring.exception.AppException;
import com.example.dormitoryspring.services.ContractService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contract")
@Slf4j
public class ContractController {
    @Autowired
    private ContractService contractService;

    @GetMapping
    public ResponseEntity<?> getAllContracts() {
        try {
            List<ContractResponse> contracts = contractService.getAllContracts();
            return ResponseEntity.ok(contracts);
        } catch (AppException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getContractById(@PathVariable Integer id) {
        try{
            ContractResponse contract = contractService.getContractById(id);
         if (contract == null) {
             return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Contract not found");
         }
            return ResponseEntity.ok(contract);
        }catch (AppException e){
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> addContract(@RequestBody ContractRequest request) {
        try {
            ContractResponse contract = contractService.addContract(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(contract);
        } catch (AppException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateContract(@PathVariable Integer id, @RequestBody ContractRequest request) {
        try {
            ContractResponse contract = contractService.updateContract(id, request);
            return ResponseEntity.ok(contract);
        } catch (AppException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteContract(@PathVariable Integer id) {
        try {
            contractService.deleteContract(id);
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
