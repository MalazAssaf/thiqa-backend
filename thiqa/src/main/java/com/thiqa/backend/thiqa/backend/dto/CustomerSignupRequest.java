package com.thiqa.backend.thiqa.backend.dto;

import com.thiqa.backend.thiqa.backend.entity.City;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CustomerSignupRequest(

    @NotBlank(message = "Full name is required") @Size(min = 3, max = 80, message = "Full name must be 3 to 80 characters") String fullName,

    @NotBlank(message = "Email is required") @Email(message = "Enter a valid email address") String email,

    @NotBlank(message = "Mobile number is required") @Pattern(regexp = "^05\\d{8}$", message = "Enter a valid mobile number like 05XXXXXXXX") String phone,

    @NotNull(message = "City is required") City city,
    @NotBlank(message = "Password is required") @Size(min = 6, message = "Password must be at least 6 characters") String password) {
}