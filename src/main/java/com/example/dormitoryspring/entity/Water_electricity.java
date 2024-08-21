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
@Table(name = "water_electricitys")
public class Water_electricity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_water_electricity;

    @ManyToOne
    @JoinColumn(name = "id_room", nullable = false)
    private Room room;

    private Double quantity_electricity_use_start;
    private Double quantity_electricity_use_end;
    private Double quantity_water_use_start;
    private Double quantity_water_use_end;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date date_note;
}
