package com.example.dormitoryspring.controllers;

import com.example.dormitoryspring.dto.request.RefreshRequest;
import com.example.dormitoryspring.dto.response.LoginResponse;
import com.example.dormitoryspring.dto.response.RefreshResponse;
import com.example.dormitoryspring.dto.response.RegisterResponse;
import com.example.dormitoryspring.exception.AppException;
import com.nimbusds.jose.JOSEException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import com.example.dormitoryspring.dto.request.UserRequest;
import com.example.dormitoryspring.dto.response.UserResponse;
import com.example.dormitoryspring.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid UserRequest request) {
        try {
            RegisterResponse registerResponse = authService.register(request);
            return ResponseEntity.ok(registerResponse);
        } catch (AppException e) {
            // Return a bad request status with the error message
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody UserRequest request) {
        try {
            LoginResponse loginResponse = authService.login(request);
            return ResponseEntity.ok(loginResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String token) {
        try {
            authService.logout(token);
            return ResponseEntity.ok("Logout successful");
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestHeader("Authorization") RefreshRequest request) throws ParseException, JOSEException {
       try{
           RefreshResponse response = authService.refreshToken(request);
           return ResponseEntity.ok(response);
       }catch (AppException e) {
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
       }
       catch (Exception e) {
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred."+e.getMessage());
       }

    }
}

