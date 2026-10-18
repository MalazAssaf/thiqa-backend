package com.thiqa.backend.thiqa.backend.shared;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionResponse {

  @ExceptionHandler(CustomResponseException.class)
  public ResponseEntity<GlobalResponse<Void>> handleCustomException(CustomResponseException ex) {
    return error(ex.getStatus(), ex.getMessage());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<GlobalResponse<Void>> handleValidationException(MethodArgumentNotValidException ex) {
    var errors = ex.getBindingResult().getFieldErrors().stream()
        .map(err -> new GlobalResponse.ErrorItem(err.getDefaultMessage()))
        .toList();
    return new ResponseEntity<>(new GlobalResponse<>(errors), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<GlobalResponse<Void>> handleUnreadable(HttpMessageNotReadableException ex) {
    return error(HttpStatus.BAD_REQUEST, "Invalid request data. Check the selected values.");
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<GlobalResponse<Void>> handleDataIntegrity(DataIntegrityViolationException ex) {
    return error(HttpStatus.CONFLICT, "Duplicate or invalid data provided.");
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<GlobalResponse<Void>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
    return new ResponseEntity<>(
        new GlobalResponse<>(List.of(new GlobalResponse.ErrorItem("Invalid value for " + ex.getName() + "."))),
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<GlobalResponse<Void>> handleOther(Exception ex) {
    ex.printStackTrace();
    return error(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong. Please try again.");
  }

  private ResponseEntity<GlobalResponse<Void>> error(HttpStatus status, String message) {
    var errors = List.of(new GlobalResponse.ErrorItem(message));
    return new ResponseEntity<>(new GlobalResponse<>(errors), status);
  }
}