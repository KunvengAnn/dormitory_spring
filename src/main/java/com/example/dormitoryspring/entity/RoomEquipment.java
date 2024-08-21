package com.example.dormitoryspring.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "roomequipments")
public class RoomEquipment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_room_equipment;

    @ManyToOne
    @JoinColumn(name = "id_equipment", nullable = false)
    private Equipment equipment;

    @ManyToOne
    @JoinColumn(name = "id_room", nullable = false)
    private Room room;

    private Integer quantity_equipment;
    private Boolean status;  // ( ex: like old, new, broken )
}
