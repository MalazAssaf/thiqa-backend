package com.thiqa.backend.thiqa.backend.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thiqa.backend.thiqa.backend.dto.AddressRequest;
import com.thiqa.backend.thiqa.backend.entity.Customer;
import com.thiqa.backend.thiqa.backend.service.AddressService;
import com.thiqa.backend.thiqa.backend.shared.GlobalResponse;
import com.thiqa.backend.thiqa.backend.dto.AddressResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/customer/addresses")
@RequiredArgsConstructor
public class AddressController {

  private final AddressService addressService;

  @GetMapping
  public GlobalResponse<List<AddressResponse>> list(@AuthenticationPrincipal Customer customer) {
    return new GlobalResponse<>(addressService.list(customer));
  }

  @PostMapping
  public GlobalResponse<AddressResponse> add(@AuthenticationPrincipal Customer customer,
      @Valid @RequestBody AddressRequest request) {
    return new GlobalResponse<>(addressService.add(customer, request));
  }

  @DeleteMapping("/{id}")
  public GlobalResponse<String> delete(@AuthenticationPrincipal Customer customer, @PathVariable Long id) {
    addressService.delete(customer, id);
    return new GlobalResponse<>("Address is deleted successfully!");
  }

  @PutMapping("/{id}")
  public GlobalResponse<AddressResponse> update(@AuthenticationPrincipal Customer customer,
      @PathVariable Long id,
      @Valid @RequestBody AddressRequest request) {
    return new GlobalResponse<>(addressService.update(customer, id, request));
  }

  @PatchMapping("/{id}/default")
  public GlobalResponse<List<AddressResponse>> setDefault(@AuthenticationPrincipal Customer customer,
      @PathVariable Long id) {
    return new GlobalResponse<>(addressService.setDefault(customer, id));
  }
}
