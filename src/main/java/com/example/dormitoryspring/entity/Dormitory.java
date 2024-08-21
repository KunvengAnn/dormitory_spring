package com.example.dormitoryspring.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "dormitorys")
public class Dormitory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_dormitory;
    private String dormitory_name;

    @OneToMany(mappedBy = "dormitory", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<Room> rooms;

    @OneToMany(mappedBy = "dormitory", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<Contract> contracts;

}
