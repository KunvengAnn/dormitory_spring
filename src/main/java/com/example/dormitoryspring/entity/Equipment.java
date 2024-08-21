package com.example.dormitoryspring.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "equipments")
public class Equipment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_equipment;
    private String equipment_name;
    private String equipment_type;    // ( ex: Furniture, Electronic)

    @OneToMany(mappedBy = "equipment", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RoomEquipment> roomEquipments;
}
