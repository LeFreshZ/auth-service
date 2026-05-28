package com.innowise.authservice.exception.handler;

import com.innowise.authservice.dto.ErrorResponse;
import com.innowise.authservice.exception.InvalidCredentialsException;
import com.innowise.authservice.exception.InvalidRefreshTokenException;
import com.innowise.authservice.exception.LoginAlreadyExistsException;
import java.time.LocalDateTime;
import org.springframework.http.ResponseEntity;
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
}
