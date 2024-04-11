package com.example.weblogin.config.Exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

	// 상품을 찾을 수 없을 때 발생하는 예외 처리
	@ExceptionHandler(ItemNotFoundException.class)
	public ResponseEntity<Object> handleProductNotFoundException(ItemNotFoundException ex, WebRequest request) {
		Map<String, Object> body = new LinkedHashMap<>();
		body.put("timestamp", LocalDateTime.now());
		body.put("message", "요청하신 상품을 찾을 수 없습니다.");

		return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError)error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});
		return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
	}

	// 유효하지 않은 요청 데이터 예외 처리
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
		Map<String, Object> body = new LinkedHashMap<>();
		body.put("timestamp", LocalDateTime.now());
		body.put("message",
			ex.getMessage() != null && !ex.getMessage().isEmpty() ? ex.getMessage() : "잘못된 요청입니다. 요청 데이터를 확인해주세요.");

		return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
	}

	// 데이터베이스 관련 예외 처리
	@ExceptionHandler(DatabaseException.class)
	public ResponseEntity<Object> handleDatabaseException(DatabaseException ex, WebRequest request) {
		Map<String, Object> body = new LinkedHashMap<>();
		body.put("timestamp", LocalDateTime.now());
		body.put("message",
			ex.getMessage() != null && !ex.getMessage().isEmpty() ? ex.getMessage() : "데이터 처리 중 오류가 발생했습니다.");

		return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	// 요청 데이터 관련 예외 처리
	@ExceptionHandler(DataNotFoundException.class)
	public ResponseEntity<Object> handleDataNotFoundException(DataNotFoundException ex, WebRequest request) {
		Map<String, Object> body = new LinkedHashMap<>();
		body.put("timestamp", LocalDateTime.now());
		body.put("message",
			ex.getMessage() != null && !ex.getMessage().isEmpty() ? ex.getMessage() : "요청하신 데이터를 찾을 수 없습니다.");

		return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
	}

	// 기타 모든 예외 처리
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
		Map<String, Object> body = new LinkedHashMap<>();
		body.put("timestamp", LocalDateTime.now());
		body.put("message", "처리 중 예상치 못한 오류가 발생했습니다.");

		return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}

