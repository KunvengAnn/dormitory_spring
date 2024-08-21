package com.example.dormitoryspring.entity;

import jakarta.persistence.*;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "contracts")
public class Contract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_contract;

    @ManyToOne
    @JoinColumn(name = "id_student", nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "id_dormitory", nullable = false)
    private Dormitory dormitory;

    @ManyToOne
    @JoinColumn(name = "id_room", nullable = false)
    private Room room;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date date_start_contract;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date date_end_contract;
}
