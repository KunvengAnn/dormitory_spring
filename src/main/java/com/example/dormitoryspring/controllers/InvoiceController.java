package com.example.dormitoryspring.controllers;

import com.example.dormitoryspring.entity.InvoiceWaterElectricity;
import com.example.dormitoryspring.services.Invoice_water_electricityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/invoice-we")
@Slf4j
public class InvoiceController {
    @Autowired
    private Invoice_water_electricityService invoiceWeSv;

    @GetMapping
    public ResponseEntity<?> getAllInvoiceWe(){
        try{
            List<InvoiceWaterElectricity> invoiceWe = invoiceWeSv.getAllInvoiceWe();
            return ResponseEntity.ok(invoiceWe);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getInvoiceWeById(@PathVariable Integer id){
        try{
            Optional<InvoiceWaterElectricity> invoiceWe = invoiceWeSv.getInvoiceWeById(id);
            return ResponseEntity.ok(invoiceWe);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}
