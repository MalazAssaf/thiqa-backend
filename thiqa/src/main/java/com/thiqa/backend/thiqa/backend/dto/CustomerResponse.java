package com.thiqa.backend.thiqa.backend.dto;

import com.thiqa.backend.thiqa.backend.entity.City;

public record CustomerResponse(
        Long id,
        String fullName,
        String email,
        String phone,
        City city,
        boolean emailVerified) {
}