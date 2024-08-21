package com.example.dormitoryspring.entity;

import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "exit_rooms")
public class ExitRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_exit;

    @ManyToOne
    @JoinColumn(name = "id_student", referencedColumnName = "id_student")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "id_dormitory", referencedColumnName = "id_dormitory")
    private Dormitory dormitory;

    @ManyToOne
    @JoinColumn(name = "id_room",referencedColumnName = "id_room")
    private Room room;

    @Column(nullable = true)
    private String reasonExit;

    private boolean isExited = false;
}
