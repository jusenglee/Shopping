package com.example.weblogin.domain;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Map;

@Data
public class ApiError {
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
    private Map<String, String> errors; // 유효성 검사 오류 등 상세 정보

    // Constructors
    public ApiError() {
        this.timestamp = LocalDateTime.now();
    }

    public ApiError(HttpStatus status, String message, String path) {
        this();
        this.status = status.value();
        this.error = status.getReasonPhrase();
        this.message = message;
        this.path = path;
    }

    public ApiError(HttpStatus status, String message, String path, Map<String, String> errors) {
        this(status, message, path);
        this.errors = errors;
    }

    // Getters and Setters
    // ...
}
