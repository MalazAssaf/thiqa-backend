package com.thiqa.backend.thiqa.backend.dto;

import com.thiqa.backend.thiqa.backend.entity.City;
import com.thiqa.backend.thiqa.backend.entity.StoreCategory;

public record StoreResponse(
    Long id, String name, StoreCategory category, City city,
    String email, String phone, boolean emailVerified) {
}