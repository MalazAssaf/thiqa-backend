package com.thiqa.backend.thiqa.backend.dto;

import com.thiqa.backend.thiqa.backend.entity.City;
import com.thiqa.backend.thiqa.backend.entity.StoreCategory;

public record StoreCardResponse(Long id, String name, StoreCategory category, City city) {
}