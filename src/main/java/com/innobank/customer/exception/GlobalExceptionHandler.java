package com.innobank.customer.exception;

import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<String> handleCustomerExiists(DuplicateEmailException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> handleDataIntregrityViolation(
            DataIntegrityViolationException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                Map.of(
                        "error", "Data intregrity violation",
                        "message", "The request conflicts with existing data or violates a data constraint."));
    }

}
