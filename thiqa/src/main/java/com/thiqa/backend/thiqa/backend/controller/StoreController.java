package com.thiqa.backend.thiqa.backend.controller;

import com.thiqa.backend.thiqa.backend.dto.ProductCreateRequest;
import com.thiqa.backend.thiqa.backend.dto.ProductResponse;
import com.thiqa.backend.thiqa.backend.dto.ProductUpdateRequest;
import com.thiqa.backend.thiqa.backend.dto.StoreResponse;
import com.thiqa.backend.thiqa.backend.entity.Store;
import com.thiqa.backend.thiqa.backend.service.ProductService;
import com.thiqa.backend.thiqa.backend.service.StoreService;
import com.thiqa.backend.thiqa.backend.shared.GlobalResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/store")
@RequiredArgsConstructor
public class StoreController {

  private final StoreService storeService;
  private final ProductService productService;

  @GetMapping("/me")
  public GlobalResponse<StoreResponse> me(@AuthenticationPrincipal Store store) {
    return new GlobalResponse<>(storeService.me(store));
  }

  @GetMapping("/products")
  public GlobalResponse<List<ProductResponse>> myProducts(@AuthenticationPrincipal Store store) {
    return new GlobalResponse<>(productService.listForStore(store));
  }

  @PostMapping(value = "/products", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  public GlobalResponse<ProductResponse> addProduct(@AuthenticationPrincipal Store store,
      @Valid @ModelAttribute ProductCreateRequest request) throws IOException {
    return new GlobalResponse<>(productService.create(store, request));
  }

  @PutMapping(value = "/products/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public GlobalResponse<ProductResponse> updateProduct(@AuthenticationPrincipal Store store,
      @PathVariable Long id,
      @Valid @ModelAttribute ProductUpdateRequest request) throws IOException {
    return new GlobalResponse<>(productService.update(store, id, request));
  }

  @DeleteMapping("/products/{id}")
  public GlobalResponse<String> deleteProduct(@AuthenticationPrincipal Store store,
      @PathVariable Long id) {
    productService.delete(store, id);
    return new GlobalResponse<>("Product deleted.");
  }
}