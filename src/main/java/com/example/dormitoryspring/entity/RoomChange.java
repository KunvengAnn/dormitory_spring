package com.example.dormitoryspring.entity;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "room_chanages")
public class RoomChange {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_room_change;

    @ManyToOne
    @JoinColumn(name = "id_student",referencedColumnName = "id_student")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "id_dormitory",referencedColumnName = "id_dormitory")
    private Dormitory dormitory;

    @Column(nullable = true)
    private String reasonChange;

    private Boolean isRoomChanged = false;


}
