package com.example.dormitoryspring.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "students")
public class Student {
    @Id
    @Column(name = "id_student", unique = true, nullable = false)
    private String id_student;

    private String student_name;
    private Boolean student_sex;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date date_of_birth_student;
    private String student_class;
    private String department_of_student;
    private String student_phone;
    private String citizenIdentification;
    private String student_imageUrl;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Contract> contracts;
}
