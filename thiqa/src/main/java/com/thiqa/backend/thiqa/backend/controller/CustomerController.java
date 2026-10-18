package com.thiqa.backend.thiqa.backend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.thiqa.backend.thiqa.backend.dto.StoreCardResponse;
import com.thiqa.backend.thiqa.backend.entity.City;
import com.thiqa.backend.thiqa.backend.entity.StoreCategory;
import com.thiqa.backend.thiqa.backend.service.StoreService;
import com.thiqa.backend.thiqa.backend.shared.GlobalResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/customer")
@RequiredArgsConstructor
public class CustomerController {

  private final StoreService storeService;

  @GetMapping("/stores")
  public GlobalResponse<List<StoreCardResponse>> stores(
      @RequestParam(required = false) StoreCategory category,
      @RequestParam(required = false) City city) {
    return new GlobalResponse<>(storeService.listStores(category, city));
  }

}
