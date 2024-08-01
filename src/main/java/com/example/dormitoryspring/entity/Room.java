package com.example.dormitoryspring.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "rooms")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_room;

    private Integer room_number;
    private Double room_price;
    private Integer room_max_capacity;
    private Integer quantity_person;
    private Boolean status_is_empty_room;

    @Column(nullable = true)
    private Boolean status_room_is_boy;

    @ManyToOne
    @JoinColumn(name = "id_dormitory", nullable = false)
    private Dormitory dormitory;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Contract> contracts;


//    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
//    private Set<Invoice_water_electricity> invoiceWaterElectricities;
//    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
//    private Set<RoomEquipment> roomEquipments;
//
//    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
//    private Set<Water_electricity> waterElectricities;

}

