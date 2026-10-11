package com.thiqa.backend.thiqa.backend.shared;

import lombok.Getter;

import java.util.List;

@Getter
public class GlobalResponse<T> {

  public static final String SUCCESS = "success";
  public static final String ERROR = "error";

  private final String status;
  private final T data;
  private final List<ErrorItem> errors;

  public GlobalResponse(List<ErrorItem> errors) {
    this.status = ERROR;
    this.data = null;
    this.errors = errors;
  }

  public GlobalResponse(T data) {
    this.status = SUCCESS;
    this.data = data;
    this.errors = null;
  }

  public record ErrorItem(String message) {
  }
}