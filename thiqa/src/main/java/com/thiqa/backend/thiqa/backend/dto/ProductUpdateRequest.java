package com.thiqa.backend.thiqa.backend.dto;

import jakarta.validation.constraints.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

public record ProductUpdateRequest(
    @NotBlank(message = "Product name is required") @Size(max = 80, message = "Product name must be under 80 characters") String name,

    @Size(max = 300, message = "Description must be under 300 characters") String description,

    @NotNull(message = "Price is required") @DecimalMin(value = "0.01", message = "Price must be greater than 0") BigDecimal price,

    @NotNull(message = "Stock is required") @Min(value = 0, message = "Stock can't be negative") Integer stock,

    MultipartFile image) {
}