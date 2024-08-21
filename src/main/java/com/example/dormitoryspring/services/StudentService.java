package com.example.dormitoryspring.services;

import com.example.dormitoryspring.dto.FileUploadDto;
import com.example.dormitoryspring.dto.StudentDTO;
import com.example.dormitoryspring.dto.response.ContractResponse;
import com.example.dormitoryspring.dto.response.StudentResponse;
import com.example.dormitoryspring.entity.Contract;
import com.example.dormitoryspring.entity.Student;
import com.example.dormitoryspring.entity.User;
import com.example.dormitoryspring.exception.AppException;
import com.example.dormitoryspring.exception.ErrorCode;
import com.example.dormitoryspring.repositories.ContractRepository;
import com.example.dormitoryspring.repositories.StudentRepository;
import com.example.dormitoryspring.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private UserRepository userRepository;

    public List<StudentResponse> getAllStudents() {
        return studentRepository.findAll().stream()
                .map(student -> {
                    StudentResponse response = new StudentResponse();
                    response.setId_student(student.getId_student());
                    response.setStudent_name(student.getStudent_name());
                    response.setStudent_sex(student.getStudent_sex());
                    response.setDate_of_birth_student(student.getDate_of_birth_student());
                    response.setStudent_class(student.getStudent_class());
                    response.setDepartment_of_student(student.getDepartment_of_student());
                    response.setStudent_phone(student.getStudent_phone());
                    response.setCitizenIdentification(student.getCitizenIdentification());
                    response.setStudent_imageUrl(student.getStudent_imageUrl());

                    List<ContractResponse> sortedContracts = student.getContracts().stream()
                            .map(this::convertToContractResponse)
                            // Sort by id contract descending
                            .sorted(Comparator.comparing(ContractResponse::getId_contract).reversed())
                            .collect(Collectors.toList());

                    Set<ContractResponse> sortedContractsSet = new LinkedHashSet<>(sortedContracts);
                    response.setContracts(sortedContractsSet);
                    return response;
                })
                .collect(Collectors.toList());
    }


    private ContractResponse convertToContractResponse(Contract contract) {
        ContractResponse response = new ContractResponse();
        response.setId_contract(contract.getId_contract());
        response.setId_student(contract.getStudent().getId_student());
        response.setId_dormitory(contract.getDormitory().getId_dormitory());
        response.setId_room(contract.getRoom().getId_room());
        response.setDate_start_contract(contract.getDate_start_contract());
        response.setDate_end_contract(contract.getDate_end_contract());
        return response;
    }


    public StudentResponse getStudentById(String id) {
        return studentRepository.findById(id)
                .map(student -> {
                    StudentResponse response = convertToStudentResponse(student);

                    // Sort the contracts by id_contract in descending
                    List<ContractResponse> sortedContracts = student.getContracts().stream()
                            .map(this::convertToContractResponse)
                            .sorted(Comparator.comparing(ContractResponse::getId_contract).reversed())
                            .collect(Collectors.toList());

                    Set<ContractResponse> sortedContractsSet = new LinkedHashSet<>(sortedContracts);
                    response.setContracts(sortedContractsSet);

                    return response;
                })
                .orElseThrow(() -> new RuntimeException("Student not found"));
    }

    private StudentResponse convertToStudentResponse(Student student) {
        StudentResponse response = new StudentResponse();
        response.setId_student(student.getId_student());
        response.setStudent_name(student.getStudent_name());
        response.setStudent_sex(student.getStudent_sex());
        response.setDate_of_birth_student(student.getDate_of_birth_student());
        response.setStudent_class(student.getStudent_class());
        response.setDepartment_of_student(student.getDepartment_of_student());
        response.setStudent_phone(student.getStudent_phone());
        response.setCitizenIdentification(student.getCitizenIdentification());
        response.setStudent_imageUrl(student.getStudent_imageUrl());

        response.setStudent_email(student.getStudent_email());
        response.setBirthPlace(student.getBirthPlace());
        response.setNationality(student.getNationality());

        Set<ContractResponse> contract = student.getContracts().stream()
                .map(this::convertToContractResponse)
                .collect(Collectors.toSet());

        response.setContracts(contract);
        return response;
    }

//    public StudentDTO uploadImageWithStudent(MultipartFile file, StudentDTO studentDTO) {
//        // Check if the file is empty
//        if (file.isEmpty()) {
//            throw new IllegalArgumentException("The file is empty.");
//        }
//        Optional<Student> st = studentRepository.findById(studentDTO.getId_student());
//        Student student = new Student();
//        if(st.isPresent()){
//            throw new RuntimeException("Student already exists!");
//        }
//
//        // Generate a unique file name
//        String originalFileName = file.getOriginalFilename();
//        if (originalFileName == null) {
//            throw new IllegalArgumentException("The file does not have a valid name.");
//        }
//        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
//        String uniqueFileName = UUID.randomUUID().toString() + fileExtension;
//
//
//         String uploadDir = System.getProperty("user.dir") + "/src/main/uploads/";
//         File uploadDirFile = new File(uploadDir);
//         if (!uploadDirFile.exists()) {
//             uploadDirFile.mkdirs();
//         }
//         String filePath = uploadDir + File.separator + uniqueFileName;
//         try {
//             File destinationFile = new File(filePath);
//             file.transferTo(destinationFile);
//         } catch (IOException e) {
//             throw new RuntimeException("Error saving file: " + e.getMessage());
//         }
//
//        // Save
//        student.setId_student(studentDTO.getId_student());
//        student.setStudent_name(studentDTO.getStudent_name());
//        student.setStudent_phone(studentDTO.getStudent_phone());
//        student.setStudent_sex(studentDTO.getStudent_sex());
//        student.setStudent_class(studentDTO.getStudent_class());
//        student.setCitizenIdentification(studentDTO.getCitizenIdentification());
//        student.setDepartment_of_student(studentDTO.getDepartment_of_student());
//        student.setDate_of_birth_student(studentDTO.getDate_of_birth_student());
//        student.setStudent_email(studentDTO.getStudent_email());
//        student.setDescriptionAboutSelf(studentDTO.getDescriptionAboutSelf());
//        student.setBirthPlace(studentDTO.getBirthPlace());
//        student.setStudent_imageUrl(uniqueFileName);
//
//        Student savedStudent = studentRepository.save(student);
//
//        studentDTO.setStudent_imageUrl(uniqueFileName);
//        return studentDTO;
//    }

    public StudentDTO uploadImageWithStudent(MultipartFile file, StudentDTO studentDTO) {
        // Check if the file is empty
        if (file.isEmpty()) {
            throw new IllegalArgumentException("The file is empty.");
        }

        Optional<Student> st = studentRepository.findById(studentDTO.getId_student());
        if(st.isPresent()){
            throw new RuntimeException("Student already exists!");
        }

        // Generate a unique file name
        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null) {
            throw new IllegalArgumentException("The file does not have a valid name.");
        }
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String uniqueFileName = UUID.randomUUID().toString() + fileExtension;

        // Define the directory to save the uploaded files
        String uploadDir = System.getProperty("user.dir") + "/src/main/uploads";
        File uploadDirFile = new File(uploadDir);

        // Create the directory if it doesn't exist
        if (!uploadDirFile.exists()) {
            uploadDirFile.mkdirs(); // Create directories if they don't exist
        }

        String filePath = uploadDir + File.separator + uniqueFileName;

        // Save file to the defined location
        try {
            File destinationFile = new File(filePath);
            file.transferTo(destinationFile);
        } catch (IOException e) {
            throw new RuntimeException("Error saving file: " + e.getMessage());
        }

        // Save the student with the image URL
        Student student = new Student();
        student.setId_student(studentDTO.getId_student());
        student.setStudent_name(studentDTO.getStudent_name());
        student.setStudent_phone(studentDTO.getStudent_phone());
        student.setStudent_sex(studentDTO.getStudent_sex());
        student.setStudent_class(studentDTO.getStudent_class());
        student.setCitizenIdentification(studentDTO.getCitizenIdentification());
        student.setDepartment_of_student(studentDTO.getDepartment_of_student());
        student.setDate_of_birth_student(studentDTO.getDate_of_birth_student());
        student.setStudent_email(studentDTO.getStudent_email());
        student.setDescriptionAboutSelf(studentDTO.getDescriptionAboutSelf());
        student.setBirthPlace(studentDTO.getBirthPlace());
        student.setStudent_imageUrl(uniqueFileName);

        studentRepository.save(student);
        studentDTO.setStudent_imageUrl(uniqueFileName);
        return studentDTO;
    }



    public Student saveStudent(StudentDTO studentDTO) {
        try {
            Optional<Student> existingStudentOpt = studentRepository.findById(studentDTO.getId_student());

            if (existingStudentOpt.isPresent()) {
                throw new RuntimeException("Student already exists!");
            }

            Student nSt = new Student();
            nSt.setId_student(studentDTO.getId_student());
            nSt.setStudent_name(studentDTO.getStudent_name());
            nSt.setStudent_phone(studentDTO.getStudent_phone());
            nSt.setStudent_sex(studentDTO.getStudent_sex());
            nSt.setStudent_class(studentDTO.getStudent_class());
            nSt.setCitizenIdentification(studentDTO.getCitizenIdentification());
            nSt.setDepartment_of_student(studentDTO.getDepartment_of_student());
            nSt.setDate_of_birth_student(studentDTO.getDate_of_birth_student());
            nSt.setStudent_email(studentDTO.getStudent_email());
            nSt.setDescriptionAboutSelf(studentDTO.getDescriptionAboutSelf());
            nSt.setBirthPlace(studentDTO.getBirthPlace());
            nSt.setStudent_email(studentDTO.getStudent_email());
            nSt.setNationality(studentDTO.getNationality());

            return studentRepository.save(nSt);
        } catch (Exception e) {
            throw new RuntimeException("Error saving student: " + e.getMessage());
        }
    }

    public Student updateStudent(String id, StudentDTO studentDTO) {
        try {
            Optional<Student> optionalStudent = studentRepository.findById(id);

            if (optionalStudent.isEmpty()) {
                throw new IllegalArgumentException("Student not found.");
            }

            Student student = optionalStudent.get();
            student.setId_student(optionalStudent.get().getId_student());
            student.setStudent_name(optionalStudent.get().getStudent_name());
            student.setStudent_phone(optionalStudent.get().getStudent_phone());
            student.setStudent_sex(optionalStudent.get().getStudent_sex());
            student.setStudent_class(optionalStudent.get().getStudent_class());
            student.setCitizenIdentification(optionalStudent.get().getCitizenIdentification());
            student.setDepartment_of_student(optionalStudent.get().getDepartment_of_student());
            student.setDate_of_birth_student(optionalStudent.get().getDate_of_birth_student());
            student.setStudent_email(optionalStudent.get().getStudent_email());
            student.setDescriptionAboutSelf(optionalStudent.get().getDescriptionAboutSelf());
            student.setBirthPlace(optionalStudent.get().getBirthPlace());
            student.setContracts(optionalStudent.get().getContracts());
            student.setStudent_imageUrl(optionalStudent.get().getStudent_imageUrl());

            return studentRepository.save(student);
        } catch (Exception e) {
            throw new RuntimeException("Error updating student: " + e.getMessage());
        }
    }

    @Transactional
    public void deleteStudent(String id, String email) {
        Optional<Student> studentOptional = studentRepository.findById(id);

        if (!studentOptional.isPresent()) {
            throw new RuntimeException("Student not found");
        }

        Student student = studentOptional.get();

        Set<Contract> contracts = student.getContracts();
        if (contracts != null && !contracts.isEmpty()) {
            contracts.forEach(contract -> {
                // Remove contract (this will automatically handle the cascade if configured)
                contractRepository.delete(contract);
            });
        }

        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user.getStudent() != null && user.getStudent().getId_student().equals(id)) {
                user.setStudent(null);
                userRepository.save(user);
            }
        }
        studentRepository.delete(student);
    }

}
