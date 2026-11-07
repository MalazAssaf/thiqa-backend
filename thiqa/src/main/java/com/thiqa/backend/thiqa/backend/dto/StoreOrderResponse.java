package com.thiqa.backend.thiqa.backend.dto;

import com.thiqa.backend.thiqa.backend.entity.OrderStatus;
import com.thiqa.backend.thiqa.backend.entity.PaymentMethod;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record StoreOrderResponse(
    Long id,
    OrderStatus status,
    PaymentMethod paymentMethod,
    boolean delayed,
    Integer scoreAtOrder,
    BigDecimal total,
    LocalDateTime createdAt,
    LocalDateTime closedAt,
    List<OrderResponse.Item> items) {
}