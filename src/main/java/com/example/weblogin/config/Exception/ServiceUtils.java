package com.example.weblogin.config.Exception;

import java.util.Optional;

public class ServiceUtils extends RuntimeException {
    public ServiceUtils() {
        super();
    }

    public ServiceUtils(String message) {
        super(message);
    }

    public ServiceUtils(String message, Throwable cause) {
        super(message, cause);
    }
    //예외 처리를 위한 메소드
    public static <T> T getOrThrow(Optional<T> optional, String errorMessage) {
        return optional.orElseThrow(() -> new IllegalStateException(errorMessage));
    }
}