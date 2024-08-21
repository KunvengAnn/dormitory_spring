package com.example.dormitoryspring.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "invoice_water_electricitys")
public class InvoiceWaterElectricity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_invoice_w_e;

    @ManyToOne
    @JoinColumn(name = "id_room")
    private Room room;

    private Double total_amount_water_electricity;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date created_at;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date update_at;
}
