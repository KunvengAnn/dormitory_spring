package com.example.dormitoryspring.dto.response;

import com.example.dormitoryspring.entity.Contract;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.Date;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentResponse {
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
    private String student_email;

    private String BirthPlace;
    private String nationality;
    private String descriptionAboutSelf;//optional

    private Set<ContractResponse> contracts;
}
