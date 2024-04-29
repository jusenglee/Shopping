package com.example.weblogin.config.Exception;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {
	private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(GlobalExceptionHandler.class);

	private String getExceptionMessageWithFallback(String message, String fallback) {
		return (message != null && !message.isEmpty()) ? message : fallback;
	}

	/**
	 * 상품을 찾을 수 없을 때 발생하는 예외 처리
	 * @param ex
	 * @param request
	 * @return
	 */
	@ExceptionHandler(ItemNotFoundException.class)
	public ResponseEntity<Object> handleProductNotFoundException(ItemNotFoundException ex, WebRequest request) {
		Map<String, Object> body = new LinkedHashMap<>();
		body.put("timestamp", LocalDateTime.now());
		body.put("message", "요청하신 상품을 찾을 수 없습니다.");
		logger.error("상품 정보를 찾을 수 없음 : {}", ex.getMessage(), ex);
		return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
	}

	/**
	 * 유효성 검사 실패시 핸들러
	 * @param ex
	 * @return
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError)error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});
		logger.error("유효성 검사 실패: {}", errors);
		return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
	}

	/**
	 * 유효하지 않은 요청 데이터 예외 처리
	 * @param ex
	 * @param request
	 * @return
	 */
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
		Map<String, Object> body = new LinkedHashMap<>();
		body.put("timestamp", LocalDateTime.now());
		body.put("message", getExceptionMessageWithFallback("잘못된 요청입니다. 요청 데이터를 확인해주세요.", ex.getMessage()));
		logger.error("잘못된 요청. : {}", ex.getMessage(), ex);
		return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
	}

	/**
	 * 데이터베이스 관련 예외 처리
	 * @param ex
	 * @param request
	 * @return
	 */
	@ExceptionHandler(DatabaseException.class)
	public ResponseEntity<Object> handleDatabaseException(DatabaseException ex, WebRequest request) {
		Map<String, Object> body = new LinkedHashMap<>();
		body.put("timestamp", LocalDateTime.now());
		body.put("message", getExceptionMessageWithFallback("데이터 처리 중 오류가 발생했습니다.", ex.getMessage()));
		logger.error("데이터 처리 중 오류. : {}", ex.getMessage(), ex);
		return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	/**
	 * 요청 데이터 관련 예외 처리
	 * @param ex
	 * @param request
	 * @return
	 */
	@ExceptionHandler(DataNotFoundException.class)
	public ResponseEntity<Object> handleDataNotFoundException(DataNotFoundException ex, WebRequest request) {
		Map<String, Object> body = new LinkedHashMap<>();
		body.put("timestamp", LocalDateTime.now());
		body.put("message", getExceptionMessageWithFallback("요청하신 데이터를 찾을 수 없습니다.", ex.getMessage()));
		logger.error("요청 데이터 오류. : {}", ex.getMessage(), ex);
		return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
	}

	/**
	 * 기타 모든 예외 처리
	 * @param ex
	 * @param request
	 * @return
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
		Map<String, Object> body = new LinkedHashMap<>();
		body.put("timestamp", LocalDateTime.now());
		body.put("message", "처리 중 예상치 못한 오류가 발생했습니다.");
		logger.error("Exception 기타 오류. : {}", ex.getMessage(), ex);
		return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(IOException.class)
	public ResponseEntity<String> handleIOException(IOException ex) {
		Map<String, Object> body = new LinkedHashMap<>();
		body.put("timestamp", LocalDateTime.now());
		body.put("message", "처리 중 예상치 못한 오류가 발생했습니다.");
		logger.error("파일 처리 중 오류: {}", ex.getMessage(), ex);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("파일 처리 중 오류가 발생했습니다.");
	}

}

