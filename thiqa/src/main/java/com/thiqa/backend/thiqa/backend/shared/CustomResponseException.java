package com.thiqa.backend.thiqa.backend.shared;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public class CustomResponseException extends RuntimeException {
  private final HttpStatus status;
  private final String message;

  public static CustomResponseException badCredentials() {
    return new CustomResponseException(HttpStatus.UNAUTHORIZED, "Incorrect mobile number or password.");
  }

  public static CustomResponseException resourceNotFoundException(String message) {
    return new CustomResponseException(HttpStatus.NOT_FOUND, message);
  }

  public static CustomResponseException badRequestException(String message) {
    return new CustomResponseException(HttpStatus.BAD_REQUEST, message);
  }

  public static CustomResponseException forbiddenException(String message) {
    return new CustomResponseException(HttpStatus.FORBIDDEN, message);
  }

  public static CustomResponseException conflictException(String message) {
    return new CustomResponseException(HttpStatus.CONFLICT, message);
  }
}