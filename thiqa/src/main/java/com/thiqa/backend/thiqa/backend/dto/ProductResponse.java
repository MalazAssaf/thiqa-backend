package com.thiqa.backend.thiqa.backend.dto;

import java.math.BigDecimal;

public record ProductResponse(Long id, String name, String description, BigDecimal price, String imageUrl,
    int stock) {
}
