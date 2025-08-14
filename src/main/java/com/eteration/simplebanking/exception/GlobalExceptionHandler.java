// src/main/java/com/eteration/simplebanking/exception/RestExceptionHandler.java
package com.eteration.simplebanking.exception;

import com.eteration.simplebanking.dto.TransactionResult;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Map;
import java.util.stream.Collectors;

import static com.eteration.simplebanking.constants.ErrorMessages.*;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<Map<String, String>> insufficient(InsufficientBalanceException exception) {
        return build(HttpStatus.BAD_REQUEST,
                Map.of("status", TransactionResult.DECLINED.name(),
                        "message", exception.getMessage()));
    }

    @ExceptionHandler(AccountAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> accountExists(AccountAlreadyExistsException exception) {
        return build(HttpStatus.CONFLICT,
                Map.of("status", TransactionResult.DECLINED.name(),
                        "message", exception.getMessage()));
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<Map<String, String>> accountNotFound(AccountNotFoundException exception) {
        return build(HttpStatus.NOT_FOUND,
                Map.of("status", TransactionResult.DECLINED.name(),
                        "message", exception.getMessage()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, String>> invalidParam(ConstraintViolationException exception) {
        String msg = exception.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));
        if (msg.isBlank()) msg = VALIDATION_ERROR;
        return build(HttpStatus.BAD_REQUEST,
                Map.of("status", "INVALID_REQUEST",
                        "message", msg));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> illegalArgument(IllegalArgumentException exception) {
        return build(HttpStatus.NOT_FOUND,
                Map.of("status", TransactionResult.DECLINED.name(),
                        "message", exception.getMessage())); // örn: ACCOUNT_NOT_FOUND
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> invalidBody(MethodArgumentNotValidException exception) {
        String msg = exception.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .findFirst()
                .orElse(VALIDATION_ERROR);
        return build(HttpStatus.BAD_REQUEST,
                Map.of("status", "INVALID_REQUEST",
                        "message", msg));
    }


    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> badJson(HttpMessageNotReadableException exception) {
        return build(HttpStatus.BAD_REQUEST,
                Map.of("status", "INVALID_REQUEST",
                        "message", MALFORMED_JSON));
    }


    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> dataIntegrity(DataIntegrityViolationException exception) {
        return build(HttpStatus.CONFLICT,
                Map.of("status", TransactionResult.DECLINED.name(),
                        "message", DATA_INTEGRITY_VIOLATION));
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> general(Exception exception) {
        String msg = (exception.getMessage() != null && !exception.getMessage().isBlank())
                ? exception.getMessage() : UNEXPECTED_ERROR;
        return build(HttpStatus.INTERNAL_SERVER_ERROR,
                Map.of("status", TransactionResult.DECLINED.name(),
                        "message", msg));
    }


    private ResponseEntity<Map<String, String>> build(HttpStatus status, Map<String, String> body) {
        return ResponseEntity.status(status).contentType(MediaType.APPLICATION_JSON).body(body);
    }
}
