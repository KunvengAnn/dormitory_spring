package com.example.dormitoryspring.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "repairs")
public class Repair {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_repair;

    @ManyToOne
    @JoinColumn(name = "id_room",referencedColumnName = "id_room")
    private Room room;

    @ManyToOne
    @JoinColumn(name = "id_dormitory",referencedColumnName = "id_dormitory")
    private Dormitory dormitory;

    @ManyToOne
    @JoinColumn(name = "id_student", referencedColumnName = "id_student")
    private Student student;

    @Column(nullable = true)
    private String reasonRepair;

    private Boolean isRepaired = false;
}
