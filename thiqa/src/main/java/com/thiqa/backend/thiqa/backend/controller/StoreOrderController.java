package com.thiqa.backend.thiqa.backend.controller;

import com.thiqa.backend.thiqa.backend.dto.StoreOrderResponse;
import com.thiqa.backend.thiqa.backend.entity.Store;
import com.thiqa.backend.thiqa.backend.service.OrderService;
import com.thiqa.backend.thiqa.backend.shared.GlobalResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/store/orders")
@RequiredArgsConstructor
public class StoreOrderController {

  private final OrderService orderService;

  @GetMapping
  public GlobalResponse<List<StoreOrderResponse>> list(@AuthenticationPrincipal Store store) {
    return new GlobalResponse<>(orderService.storeOrders(store));
  }

  @PatchMapping("/{id}/confirm")
  public GlobalResponse<StoreOrderResponse> confirm(@AuthenticationPrincipal Store store, @PathVariable Long id) {
    return new GlobalResponse<>(orderService.confirm(store, id));
  }

  @PatchMapping("/{id}/reject")
  public GlobalResponse<StoreOrderResponse> reject(@AuthenticationPrincipal Store store, @PathVariable Long id) {
    return new GlobalResponse<>(orderService.reject(store, id));
  }

  @PatchMapping("/{id}/out-for-delivery")
  public GlobalResponse<StoreOrderResponse> outForDelivery(@AuthenticationPrincipal Store store,
      @PathVariable Long id) {
    return new GlobalResponse<>(orderService.markOutForDelivery(store, id));
  }

  @PatchMapping("/{id}/delayed")
  public GlobalResponse<StoreOrderResponse> delayed(@AuthenticationPrincipal Store store, @PathVariable Long id) {
    return new GlobalResponse<>(orderService.markDelayed(store, id));
  }

  @PatchMapping("/{id}/delivered")
  public GlobalResponse<StoreOrderResponse> delivered(@AuthenticationPrincipal Store store, @PathVariable Long id) {
    return new GlobalResponse<>(orderService.markDelivered(store, id));
  }

  @PatchMapping("/{id}/refused")
  public GlobalResponse<StoreOrderResponse> refused(@AuthenticationPrincipal Store store, @PathVariable Long id) {
    return new GlobalResponse<>(orderService.markRefused(store, id));
  }
}