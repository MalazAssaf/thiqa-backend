package com.thiqa.backend.thiqa.backend.dto;

import com.thiqa.backend.thiqa.backend.entity.City;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AddressRequest(
    @NotBlank(message = "Label is required.") @Size(max = 30) String label,
    @NotNull(message = "City is required.") City city,
    @NotBlank(message = "Address is required.") @Size(max = 200) String details) {
}
