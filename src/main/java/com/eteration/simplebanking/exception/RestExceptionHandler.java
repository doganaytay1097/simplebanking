package com.eteration.simplebanking.exception;

import com.eteration.simplebanking.dto.TransactionResult;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.eteration.simplebanking.constants.ErrorMessages.*;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(InsufficientBalanceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> insufficient(InsufficientBalanceException e) {

        return Map.of(
                "status", TransactionResult.DECLINED.name(),
                "message", e.getMessage()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> invalid(MethodArgumentNotValidException e) {
        String msg = e.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .orElse(VALIDATION_ERROR); // <-- hardcoded yerine sabit

        return Map.of(
                "status", "INVALID_REQUEST",
                "message", msg
        );
    }
}
