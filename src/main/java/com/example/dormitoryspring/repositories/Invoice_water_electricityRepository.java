package com.example.dormitoryspring.repositories;

import com.example.dormitoryspring.entity.InvoiceWaterElectricity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Invoice_water_electricityRepository extends JpaRepository<InvoiceWaterElectricity,Integer> {

}
