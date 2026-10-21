package com.thiqa.backend.thiqa.backend.dto;

import java.util.List;

import com.thiqa.backend.thiqa.backend.entity.City;
import com.thiqa.backend.thiqa.backend.entity.StoreCategory;

public record StoreDetailResponse(
    Long id, String name, StoreCategory category, City city,
    List<ProductResponse> products) {
}
