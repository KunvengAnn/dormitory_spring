package com.example.dormitoryspring.controllers;

import com.example.dormitoryspring.dto.request.OnlyUpdateUserRequest;
import com.example.dormitoryspring.dto.request.UserRequest;
import com.example.dormitoryspring.dto.response.UserResponse;
import com.example.dormitoryspring.exception.AppException;
import com.example.dormitoryspring.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllUsers() {
        try {
            List<UserResponse> users = userService.getAllUsers();
            return ResponseEntity.ok(users);
        } catch (AppException e) {
            log.error("Error retrieving users: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping("/user-update")
    public ResponseEntity<?> updateUser(@RequestBody UserRequest userRequest) {
        try {
            UserResponse userResponse = userService.updateUser(userRequest);
            return ResponseEntity.ok(userResponse);
        }catch (AppException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    //
    @PutMapping("/user-update-id-student")
    public ResponseEntity<?> updateUserWithIdStudent(@RequestBody OnlyUpdateUserRequest onlyUpdateUserRequest){
        try {
            UserResponse updatedUserResponse = userService.updateOnlyUser(onlyUpdateUserRequest);
            return ResponseEntity.ok(updatedUserResponse);
        }catch (AppException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}
