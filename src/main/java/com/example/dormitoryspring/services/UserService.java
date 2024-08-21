package com.example.dormitoryspring.services;

import com.example.dormitoryspring.dto.request.OnlyUpdateUserRequest;
import com.example.dormitoryspring.dto.request.UserRequest;
import com.example.dormitoryspring.dto.response.UserResponse;
import com.example.dormitoryspring.entity.Student;
import com.example.dormitoryspring.entity.User;
import com.example.dormitoryspring.enums.Role;
import com.example.dormitoryspring.exception.AppException;
import com.example.dormitoryspring.exception.ErrorCode;
import com.example.dormitoryspring.repositories.StudentRepository;
import com.example.dormitoryspring.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentRepository studentRepository;


    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        // Convert List<User> to List<UserResponse>
        return users.stream()
                .map(this::convertToUserResponse)
                .toList();
    }

    // Get user by ID
    public UserResponse getUserById(Integer id) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isPresent()) {
            return convertToUserResponse(userOpt.get());
        } else {
            throw new RuntimeException("User not found with id: " + id);
        }
    }

    @Transactional
    public UserResponse createUser(UserRequest userRequest) {
        if (userRepository.existsByEmail(userRequest.getEmail())) {
            throw new RuntimeException("User with this email already exists");
        }

        User newUser = User.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .email(userRequest.getEmail())
                .password(userRequest.getPassword())
                .role(userRequest.getRole() != null ? userRequest.getRole() : Role.USER)
                .build();

        User savedUser = userRepository.save(newUser);
        return convertToUserResponse(savedUser);
    }


    // Update user
    @Transactional
    public UserResponse updateUser(UserRequest updatedUser) {
        Optional<User> userOpt = userRepository.findByEmail(updatedUser.getEmail());
        if (userOpt.isPresent()) {
            User existingUser = userOpt.get();
            // Update fields
            existingUser.setFirstName(updatedUser.getFirstName());
            existingUser.setLastName(updatedUser.getLastName());
            existingUser.setEmail(updatedUser.getEmail());

            // Update student association if provided
            if (updatedUser.getId_student() != null) {
                Student student = studentRepository.findById(updatedUser.getId_student())
                        .orElseThrow(() -> new RuntimeException("Student not found with id: " + updatedUser.getId_student()));
                existingUser.setStudent(student);
            }

            // Save updated user
            User savedUser = userRepository.save(existingUser);
            return convertToUserResponse(savedUser);
        } else {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
    }

    @Transactional
    public UserResponse updateOnlyUser(OnlyUpdateUserRequest onlyUpdateUserRequest){
        Optional<User> userOptional = userRepository.findByEmail(onlyUpdateUserRequest.getEmail());
        if (userOptional.isPresent()) {
            User existingUser = userOptional.get();

            if (onlyUpdateUserRequest.getId_student() != null) {
                Optional<Student> studentOpt = studentRepository.findById(onlyUpdateUserRequest.getId_student());
                if (studentOpt.isPresent()) {
                    Student student = studentOpt.get();
                    existingUser.setStudent(student);
                } else {
                    throw new RuntimeException("Student not found!");
                }
            }

            User savedUser = userRepository.save(existingUser);
            return convertToUserResponse(savedUser);
        }else{
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
    }

    // Delete user
    @Transactional
    @PreAuthorize("hasRole('ADMIN')") // Restrict access to ADMIN only
    public void deleteUser(Integer id) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isPresent()) {
            userRepository.delete(userOpt.get());
        } else {
            throw new RuntimeException("User not found with id: " + id);
        }
    }

    // Convert User entity to UserResponse DTO
    private UserResponse convertToUserResponse(User user) {
        return UserResponse.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .password(user.getPassword())
                .id_student(user.getStudent().getId_student())
                .role(user.getRole())
                .build();
    }
}
