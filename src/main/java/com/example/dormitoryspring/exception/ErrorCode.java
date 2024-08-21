package com.example.dormitoryspring.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "Invalid key", HttpStatus.BAD_REQUEST),
    USER_EXISTED(1002, "User existed", HttpStatus.BAD_REQUEST),

    STUDENT_EXISTED(2001,"Student Already exists!",HttpStatus.BAD_REQUEST),

    USERNAME_INVALID(1003, "Username must be at least {min} characters", HttpStatus.BAD_REQUEST),
    INVALID_MIN_PASSWORD(1004, "Password must be at least {min} characters", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1005,"Invalid credentials Or Password wrong !", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(1005, "User not found", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1006, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1007, "You do not have permission", HttpStatus.FORBIDDEN),
    INVALID_DOB(1008, "Your age must be at least {min}", HttpStatus.BAD_REQUEST),

    INVALID_TOKEN_SIGNATURE(1009,"Invalid token signature",HttpStatus.BAD_REQUEST),
    INVALID_TOKEN_EXPIRED(1010,"Token expired",HttpStatus.BAD_REQUEST),
    TOKEN_INVALIDATED(1011,"Token is invalidated",HttpStatus.BAD_REQUEST);

    private final int code;
    private final String message;
    private final HttpStatus httpStatus;

    ErrorCode(int code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }

}
