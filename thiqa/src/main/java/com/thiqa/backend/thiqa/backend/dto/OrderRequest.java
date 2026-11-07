package com.thiqa.backend.thiqa.backend.dto;

import com.thiqa.backend.thiqa.backend.entity.PaymentMethod;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record OrderRequest(
    @NotNull(message = "Store is required.") Long storeId,

    @NotNull(message = "Payment method is required.") PaymentMethod paymentMethod,

    @NotEmpty(message = "Your cart is empty.") List<@Valid Item> items) {
  public record Item(
      @NotNull(message = "Product is required") Long productId,
      @NotNull(message = "Quantity is required.") @Min(value = 1, message = "Quantity must be at least 1.") Integer quantity) {
  }
}