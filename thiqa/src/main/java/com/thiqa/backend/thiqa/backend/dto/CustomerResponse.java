package com.thiqa.backend.thiqa.backend.dto;

import java.time.LocalDateTime;

import com.thiqa.backend.thiqa.backend.entity.City;

public record CustomerResponse(
    Long id,
    String fullName,
    String email,
    String phone,
    City city,
    boolean emailVerified,
    LocalDateTime createdAt) {
}