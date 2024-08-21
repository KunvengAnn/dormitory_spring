package com.example.dormitoryspring.entity;

import com.example.dormitoryspring.enums.Role;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "users")
@Getter
@Setter

/* Purpose: Provides a builder pattern implementation for the class.
   @Builder allows you to create instances of the User class using a builder pattern,
   which can make object creation more readable and flexible.
 */
@Builder

// Purpose: Generates a no-argument constructor for the class.
@NoArgsConstructor
// Purpose: Generates a constructor with arguments for all fields in the class.
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    @ManyToOne
    @JoinColumn(name = "id_student", referencedColumnName = "id_student")
    private Student student;

    @Enumerated(EnumType.STRING)
    private Role role;
}

/*
https://drive.google.com/file/d/1lSLzrG0wd5oTy3qaan6EUNG72Nt-ywEB/view?usp=sharing
* */
