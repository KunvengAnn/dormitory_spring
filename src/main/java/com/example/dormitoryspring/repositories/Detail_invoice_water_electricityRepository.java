package com.example.dormitoryspring.repositories;

import com.example.dormitoryspring.entity.Detail_invoice_water_electricity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Detail_invoice_water_electricityRepository extends JpaRepository<Detail_invoice_water_electricity,Integer> {

}
