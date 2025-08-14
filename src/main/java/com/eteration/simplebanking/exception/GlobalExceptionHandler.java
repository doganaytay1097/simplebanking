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

    // --- Domain hataları ---
    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<Map<String, String>> insufficient(InsufficientBalanceException e) {
        return build(HttpStatus.BAD_REQUEST,
                Map.of("status", TransactionResult.DECLINED.name(),
                        "message", e.getMessage()));
    }

    @ExceptionHandler(AccountAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> accountExists(AccountAlreadyExistsException e) {
        return build(HttpStatus.CONFLICT,
                Map.of("status", TransactionResult.DECLINED.name(),
                        "message", e.getMessage()));
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<Map<String, String>> accountNotFound(AccountNotFoundException e) {
        return build(HttpStatus.NOT_FOUND,
                Map.of("status", TransactionResult.DECLINED.name(),
                        "message", e.getMessage()));
    }

    // --- Validation hataları (Path/Query @Validated) ---
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, String>> invalidParam(ConstraintViolationException e) {
        String msg = e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));
        if (msg.isBlank()) msg = VALIDATION_ERROR;
        return build(HttpStatus.BAD_REQUEST,
                Map.of("status", "INVALID_REQUEST",
                        "message", msg));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> illegalArgument(IllegalArgumentException e) {
        return build(HttpStatus.NOT_FOUND,
                Map.of("status", TransactionResult.DECLINED.name(),
                        "message", e.getMessage())); // örn: ACCOUNT_NOT_FOUND
    }

    // --- Validation hataları (RequestBody @Valid) ---
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> invalidBody(MethodArgumentNotValidException e) {
        String msg = e.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .findFirst()
                .orElse(VALIDATION_ERROR);
        return build(HttpStatus.BAD_REQUEST,
                Map.of("status", "INVALID_REQUEST",
                        "message", msg));
    }

    // --- JSON parse hatası ---
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> badJson(HttpMessageNotReadableException e) {
        return build(HttpStatus.BAD_REQUEST,
                Map.of("status", "INVALID_REQUEST",
                        "message", MALFORMED_JSON));
    }

    // --- Veri bütünlüğü (FK/unique vs.) ---
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> dataIntegrity(DataIntegrityViolationException e) {
        return build(HttpStatus.CONFLICT,
                Map.of("status", TransactionResult.DECLINED.name(),
                        "message", DATA_INTEGRITY_VIOLATION));
    }

    // --- Genel fallback ---
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> general(Exception e) {
        String msg = (e.getMessage() != null && !e.getMessage().isBlank())
                ? e.getMessage() : UNEXPECTED_ERROR;
        return build(HttpStatus.INTERNAL_SERVER_ERROR,
                Map.of("status", TransactionResult.DECLINED.name(),
                        "message", msg));
    }

    // ---- Ortak ResponseEntity kurucu ----
    private ResponseEntity<Map<String, String>> build(HttpStatus status, Map<String, String> body) {
        return ResponseEntity.status(status).contentType(MediaType.APPLICATION_JSON).body(body);
    }
}
