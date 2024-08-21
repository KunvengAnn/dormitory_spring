package com.example.dormitoryspring.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import java.util.Date;

@Getter
@Setter
public class StudentDTO {
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
    private String BirthPlace;
    private String nationality;
    private String student_email;
    private String descriptionAboutSelf;//optional

}
