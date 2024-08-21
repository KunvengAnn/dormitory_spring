package com.example.dormitoryspring.dto.response;

import com.example.dormitoryspring.enums.Role;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private Integer id_user;
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    private String id_student;

    private Role role;
    private String token;
}
