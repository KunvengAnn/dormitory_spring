package com.example.dormitoryspring.dto.request;

import com.example.dormitoryspring.enums.Role;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    private String firstName;
    private String lastName;
    @NotBlank(message = "Email is required")
    private String email;

    private String id_student;

    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    private Role role;
}
