package com.thiqa.backend.thiqa.backend.dto;

import com.thiqa.backend.thiqa.backend.entity.City;
import com.thiqa.backend.thiqa.backend.entity.StoreCategory;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record StoreSettingsRequest(
    @NotBlank(message = "Store name is required") @Size(min = 2, max = 80, message = "Store name be between 2 and 80 characters.") String name,
    @NotNull(message = "Category is required.") StoreCategory category,
    @NotNull(message = "City is required.") City city,
    boolean autoReject,
    @Min(value = 0, message = "Threshold must be between 0 and 100.") @Max(value = 100, message = "Threshold must be between 0 and 100.") int rejectThreshold) {
}