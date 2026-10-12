package com.thiqa.backend.thiqa.backend.dto;

import com.thiqa.backend.thiqa.backend.entity.City;
import com.thiqa.backend.thiqa.backend.entity.StoreCategory;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record StoreSignupRequest(
    @NotBlank(message = "Store name is required") @Size(min = 2, max = 80) String name,
    @NotNull(message = "Store category is required") StoreCategory category,
    @NotNull(message = "City is required") City city,
    @NotBlank(message = "Mobile number is required") @Pattern(regexp = "^05\\d{8}$", message = "Enter a valid mobile number like 05XXXXXXXX") String phone,
    @NotBlank(message = "Email is required") @Email String email,
    @NotBlank(message = "Password is required") @Size(min = 6, message = "Password must be at least 6 characters") String password) {
}
