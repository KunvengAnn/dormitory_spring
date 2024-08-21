package com.example.dormitoryspring.services;

import com.example.dormitoryspring.entity.Detail_invoice_water_electricity;
import com.example.dormitoryspring.entity.RoomEquipment;
import com.example.dormitoryspring.repositories.Detail_invoice_water_electricityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class Detail_invoice_water_electricityService {
    @Autowired
    private Detail_invoice_water_electricityRepository detailInvoiceWaterElectricityRepository;

    public List<Detail_invoice_water_electricity> getAllDetailInvoiceWaterElectricity() {
        return detailInvoiceWaterElectricityRepository.findAll();
    }

    public Optional<Detail_invoice_water_electricity> GetDetailInvoiceWaterElectricityById(Integer id) {
        Optional<Detail_invoice_water_electricity> optionalDetailInvoiceWaterElectricity = detailInvoiceWaterElectricityRepository.findById(id);
        if(optionalDetailInvoiceWaterElectricity.isPresent()) {
            return optionalDetailInvoiceWaterElectricity;
        }else{
            throw new RuntimeException("Detail invoice water electricity not found");
        }
    }
}
