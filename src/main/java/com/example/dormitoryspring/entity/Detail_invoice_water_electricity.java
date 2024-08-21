package com.example.dormitoryspring.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "detail_invoice_water_electricitys")
public class Detail_invoice_water_electricity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_dt_invoice_w;

    @ManyToOne
    @JoinColumn(name = "id_invoice_w_e", nullable = false)
    private InvoiceWaterElectricity invoiceWaterElectricity;

    private Double water_price;
    private Double electricity_price;
    private Double total_water_price;
    private Double total_electricity_price;
}
