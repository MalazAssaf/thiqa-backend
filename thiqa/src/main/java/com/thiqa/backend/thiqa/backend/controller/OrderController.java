package com.thiqa.backend.thiqa.backend.controller;

import com.thiqa.backend.thiqa.backend.dto.OrderRequest;
import com.thiqa.backend.thiqa.backend.dto.OrderResponse;
import com.thiqa.backend.thiqa.backend.entity.Customer;
import com.thiqa.backend.thiqa.backend.service.OrderService;
import com.thiqa.backend.thiqa.backend.shared.GlobalResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer/orders")
@RequiredArgsConstructor
public class OrderController {

  private final OrderService orderService;

  @PostMapping
  public GlobalResponse<OrderResponse> place(@AuthenticationPrincipal Customer customer,
      @Valid @RequestBody OrderRequest request) {
    return new GlobalResponse<>(orderService.place(customer, request));
  }

  @GetMapping
  public GlobalResponse<List<OrderResponse>> myOrders(@AuthenticationPrincipal Customer customer) {
    return new GlobalResponse<>(orderService.myOrders(customer));
  }
}