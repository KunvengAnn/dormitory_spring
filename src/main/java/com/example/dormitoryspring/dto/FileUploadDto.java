package com.example.dormitoryspring.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;


@Getter
@Setter
public class FileUploadDto {
    private MultipartFile file;
    private StudentDTO studentDTO;
}
