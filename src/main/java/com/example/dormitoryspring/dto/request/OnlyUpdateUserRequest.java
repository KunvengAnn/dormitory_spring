package com.example.dormitoryspring.dto.request;

import lombok.*;

@Getter
@Setter
public class OnlyUpdateUserRequest {
    private String email;
    private String id_student;

}
