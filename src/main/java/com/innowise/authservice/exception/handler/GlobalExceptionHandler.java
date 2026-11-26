package com.innowise.authservice.exception.handler;

import com.innowise.authservice.dto.ErrorResponse;
import com.innowise.authservice.exception.InvalidCredentialsException;
import com.innowise.authservice.exception.InvalidRefreshTokenException;
import com.innowise.authservice.exception.LoginAlreadyExistsException;
import java.time.LocalDateTime;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler({
      InvalidRefreshTokenException.class,
      InvalidCredentialsException.class
  })
  public ResponseEntity<ErrorResponse> handleUnauthorized(RuntimeException ex) {
    ErrorResponse response = new ErrorResponse(LocalDateTime.now(), 401, ex.getMessage());

    return ResponseEntity.status(401).body(response);
  }

  @ExceptionHandler(LoginAlreadyExistsException.class)
  public ResponseEntity<ErrorResponse> handleLoginAlreadyExists(LoginAlreadyExistsException ex) {
    ErrorResponse response = new ErrorResponse(LocalDateTime.now(), 409, ex.getMessage());

    return ResponseEntity.status(409).body(response);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
    String message = ex.getBindingResult().getFieldErrors().stream()
        .map(FieldError::getDefaultMessage)
        .collect(Collectors.joining(", "));

    ErrorResponse response = new ErrorResponse(LocalDateTime.now(), 400, message);

    return ResponseEntity.status(400).body(response);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGeneral(Exception ex) {
    ErrorResponse response = new ErrorResponse(LocalDateTime.now(), 500, "Internal Server error");

    return ResponseEntity.status(500).body(response);
  }
}
