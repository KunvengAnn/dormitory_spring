package com.example.dormitoryspring.controllers;

import com.example.dormitoryspring.dto.request.InfoCurrentUserRequest;
import com.example.dormitoryspring.dto.request.IntrospectRequest;
import com.example.dormitoryspring.dto.request.RefreshRequest;
import com.example.dormitoryspring.dto.response.*;
import com.example.dormitoryspring.entity.User;
import com.example.dormitoryspring.exception.AppException;
import com.nimbusds.jose.JOSEException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import com.example.dormitoryspring.dto.request.UserRequest;
import com.example.dormitoryspring.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Optional;

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

    @PostMapping("/getCurrentUser")
    public ResponseEntity<?> getCurrentUser(@RequestBody InfoCurrentUserRequest infoCurrentUserRequest)  {
        try {
            log.error(infoCurrentUserRequest.getEmail());
            log.debug("email"+infoCurrentUserRequest.getEmail());
            Optional<LoginResponse> lgRes = authService.getCurrentUserLogin(infoCurrentUserRequest.getEmail());
            return ResponseEntity.ok(lgRes);
        }catch (AppException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/introspect")
    public ResponseEntity<?> introspect(@RequestBody IntrospectRequest request) throws ParseException, JOSEException {
        try
        {
            IntrospectResponse result = authService.introspect(request);

            return ResponseEntity.ok(result);
        }catch (AppException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}

