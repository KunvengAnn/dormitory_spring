package com.example.dormitoryspring.controllers;

import com.example.dormitoryspring.dto.FileUploadDto;
import com.example.dormitoryspring.dto.StudentDTO;
import com.example.dormitoryspring.dto.response.StudentResponse;
import com.example.dormitoryspring.entity.Student;
import com.example.dormitoryspring.exception.AppException;
import com.example.dormitoryspring.exception.ErrorCode;
import com.example.dormitoryspring.services.StudentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/student")
@RequiredArgsConstructor
@Slf4j
public class StudentController {
    @Autowired
    private StudentService studentService;

    @GetMapping("/getAllStudent")
    public ResponseEntity<?> getAllStudent() {
        try {
            List<StudentResponse> studentRes = studentService.getAllStudents();
            // Sort the list by id_student in descending order
            studentRes.sort(Comparator.comparing(StudentResponse::getId_student).reversed());
            return ResponseEntity.ok(studentRes);
        }catch (AppException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getStudentById(@PathVariable String id) {
        try {
            StudentResponse studentResponse = studentService.getStudentById(id);

            return ResponseEntity.ok(studentResponse);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/uploadImage")
    public ResponseEntity<?> uploadImageWithStudent(@RequestParam("image") MultipartFile file, @RequestParam("studentDTO") String studentDTOJson){
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            StudentDTO studentDTO = objectMapper.readValue(studentDTOJson, StudentDTO.class);
            log.info("Image uploaded successfully for student ID: " + studentDTO.getId_student());
            StudentDTO stDTO = studentService.uploadImageWithStudent(file, studentDTO);

            return ResponseEntity.ok(stDTO);
        }catch (AppException e){
            ErrorCode errorCode = e.getErrorCode();
            return ResponseEntity.status(errorCode.getHttpStatus())
                    .body(errorCode.getMessage());
        }catch (Exception e) {
            log.error("Error uploading image", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error uploading image: " + e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> saveStudent(
            @RequestParam("studentDTO") String studentDTOJson
    ) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            StudentDTO studentDTO = objectMapper.readValue(studentDTOJson, StudentDTO.class);

            Student savedStudent = studentService.saveStudent(studentDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedStudent);
        }catch (AppException e){
            ErrorCode errorCode = e.getErrorCode();
            return ResponseEntity.status(errorCode.getHttpStatus())
                    .body(errorCode.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateStudent(
            @PathVariable String id,
            @RequestParam("studentDTO") String studentDTOJson
    ) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            StudentDTO studentDTO = objectMapper.readValue(studentDTOJson, StudentDTO.class);

            Student updatedStudent = studentService.updateStudent(id, studentDTO);
            return ResponseEntity.ok(updatedStudent);
        } catch (AppException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStudent(@PathVariable String id,@RequestBody String email) {
        try {
            studentService.deleteStudent(id,email);
            return ResponseEntity.ok("Student deleted successfully.");
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}

