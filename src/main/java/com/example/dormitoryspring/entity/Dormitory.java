package com.example.dormitoryspring.entity;


import jakarta.persistence.*;
import lombok.*;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "dormitory")
public class Dormitory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_dormitory;

    private String dormitory_name;


    @OneToMany(mappedBy = "dormitory", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Room> rooms;

    @OneToMany(mappedBy = "dormitory", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Contract> contracts;

}