package com.thiqa.backend.thiqa.backend.dto;

import com.thiqa.backend.thiqa.backend.entity.City;

public record AddressResponse(Long id, String label, City city, String details, boolean defaultAddress) {
}
