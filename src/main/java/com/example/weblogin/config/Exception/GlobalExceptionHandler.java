package com.example.weblogin.config.Exception;
import com.example.weblogin.domain.ApiError;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LogManager.getLogger(GlobalExceptionHandler.class);

    /**
     * 상품을 찾을 수 없을 때 발생하는 예외 처리
     */
    @ExceptionHandler(ItemNotFoundException.class)
    public ResponseEntity<ApiError> handleItemNotFoundException(ItemNotFoundException ex, WebRequest request) {
        ApiError apiError = new ApiError(
                HttpStatus.NOT_FOUND,
                "요청하신 상품을 찾을 수 없습니다.",
                request.getDescription(false).replace("uri=", "")
        );
        logger.error("상품 정보를 찾을 수 없음 : {}", ex.getMessage(), ex);
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    /**
     * 데이터가 없을 때 발생하는 예외 처리
     */
    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<ApiError> handleDataNotFoundException(DataNotFoundException ex, WebRequest request) {
        ApiError apiError = new ApiError(
                HttpStatus.NOT_FOUND,
                "요청하신 데이터를 찾을 수 없습니다.",
                request.getDescription(false).replace("uri=", "")
        );
        logger.error("요청 데이터 오류. : {}", ex.getMessage(), ex);
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    /**
     * 유효성 검사 실패시 핸들러
     */
    @ExceptionHandler({MethodArgumentNotValidException.class, HttpMessageNotReadableException.class})
    public ResponseEntity<ApiError> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError)error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        ApiError apiError = new ApiError(
                HttpStatus.BAD_REQUEST,
                "유효성 검사에 실패했습니다. 각 항목을 다시 확인해주세요",
                request.getDescription(false).replace("uri=", ""),
                errors
        );
        logger.error("유효성 검사 실패: {}", errors);
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    /**
     * 유효하지 않은 요청 데이터 예외 처리
     */
    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<ApiError> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        ApiError apiError = new ApiError(
                HttpStatus.BAD_REQUEST,
                ex.getMessage() != null && !ex.getMessage().isEmpty() ? ex.getMessage() : "잘못된 요청입니다. 요청 데이터를 확인해주세요.",
                request.getDescription(false).replace("uri=", "")
        );
        logger.error("잘못된 요청. : {}", ex.getMessage(), ex);
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    /**
     * 데이터베이스 관련 예외 처리
     */
    @ExceptionHandler(DatabaseException.class)
    public ResponseEntity<ApiError> handleDatabaseException(DatabaseException ex, WebRequest request) {
        ApiError apiError = new ApiError(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "데이터 처리 중 오류가 발생했습니다.",
                request.getDescription(false).replace("uri=", "")
        );
        logger.error("데이터 처리 중 오류. : {}", ex.getMessage(), ex);
        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 파일 업로드 시 용량 초과 예외 처리
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ApiError> handleMaxSizeException(MaxUploadSizeExceededException ex, WebRequest request) {
        ApiError apiError = new ApiError(
                HttpStatus.PAYLOAD_TOO_LARGE,
                "업로드 가능한 파일 크기를 초과했습니다.",
                request.getDescription(false).replace("uri=", "")
        );
        logger.error("파일 업로드 용량 초과: {}", ex.getMessage(), ex);
        return new ResponseEntity<>(apiError, HttpStatus.PAYLOAD_TOO_LARGE);
    }

    /**
     * JSON 매핑 오류 처리
     */
    @ExceptionHandler(JsonMappingException.class)
    public ResponseEntity<ApiError> handleJsonMappingException(JsonMappingException ex, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getPath().forEach(reference -> {
            String fieldName = reference.getFieldName();
            errors.put(fieldName, ex.getOriginalMessage());
        });
        ApiError apiError = new ApiError(
                HttpStatus.BAD_REQUEST,
                "잘못된 JSON 형식입니다.",
                request.getDescription(false).replace("uri=", ""),
                errors
        );
        logger.error("JSON 매핑 오류: {}", errors);
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    /**
     * IOException 처리
     */
    @ExceptionHandler(IOException.class)
    public ResponseEntity<ApiError> handleIOException(IOException ex, WebRequest request) {
        ApiError apiError = new ApiError(
                HttpStatus.BAD_REQUEST,
                "파일 처리 중 오류가 발생했습니다.",
                request.getDescription(false).replace("uri=", "")
        );
        logger.error("파일 처리 중 오류: {}", ex.getMessage(), ex);
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    /**
     * 기타 모든 예외 처리
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleAllExceptions(Exception ex, WebRequest request) {
        ApiError apiError = new ApiError(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "처리 중 예상치 못한 오류가 발생했습니다.",
                request.getDescription(false).replace("uri=", "")
        );
        logger.error("Exception 기타 오류. : {}", ex.getMessage(), ex);
        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
