package com.example.dormitoryspring.dto.response;

import com.example.dormitoryspring.enums.Role;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterResponse {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Role role;
}
