package com.example.dormitoryspring.services;

import com.example.dormitoryspring.entity.InvoiceWaterElectricity;
import com.example.dormitoryspring.repositories.Invoice_water_electricityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class Invoice_water_electricityService {
    @Autowired
    private Invoice_water_electricityRepository invoice_water_electricityRepository;

    public List<InvoiceWaterElectricity> getAllInvoiceWe() {
        return invoice_water_electricityRepository.findAll();
    }

    public Optional<InvoiceWaterElectricity> getInvoiceWeById(Integer id) {
        Optional<InvoiceWaterElectricity> optIwe = invoice_water_electricityRepository.findById(id);
        if(optIwe.isPresent()) {
            return optIwe;
        }else{
            throw new RuntimeException("Invoice water electricity not found!");
        }
    }
}
