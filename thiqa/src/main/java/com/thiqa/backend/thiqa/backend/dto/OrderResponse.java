package com.thiqa.backend.thiqa.backend.dto;

import com.thiqa.backend.thiqa.backend.entity.OrderStatus;
import com.thiqa.backend.thiqa.backend.entity.PaymentMethod;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(
    Long id,
    Long storeId,
    String storeName,
    OrderStatus status,
    PaymentMethod paymentMethod,
    boolean delayed,
    BigDecimal total,
    LocalDateTime createdAt,
    LocalDateTime closedAt,
    List<Item> items) {
  public record Item(Long productId, String name, String imageUrl, int quantity, BigDecimal unitPrice) {
  }
}