package com.thiqa.backend.thiqa.backend.service;

import com.thiqa.backend.thiqa.backend.dto.StoreCardResponse;
import com.thiqa.backend.thiqa.backend.dto.StoreDetailResponse;
import com.thiqa.backend.thiqa.backend.dto.StoreResponse;
import com.thiqa.backend.thiqa.backend.entity.City;
import com.thiqa.backend.thiqa.backend.entity.Store;
import com.thiqa.backend.thiqa.backend.entity.StoreCategory;
import com.thiqa.backend.thiqa.backend.repository.StoreRepository;
import com.thiqa.backend.thiqa.backend.shared.CustomResponseException;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreService {

  private final StoreRepository storeRepository;
  private final ProductService productService;

  public StoreResponse me(Store store) {
    return new StoreResponse(store.getId(), store.getName(), store.getCategory(), store.getCity(),
        store.getEmail(), store.getPhone(), store.isEmailVerified(),
        store.isAutoReject(), store.getRejectThreshold());
  }

  public List<StoreCardResponse> listStores(StoreCategory category, City city) {
    return storeRepository.findByEmailVerifiedTrueOrderByNameAsc().stream()
        .filter(s -> category == null || s.getCategory() == category)
        .filter(s -> city == null || s.getCity() == city)
        .map(s -> new StoreCardResponse(s.getId(), s.getName(), s.getCategory(), s.getCity()))
        .toList();
  }

  public StoreDetailResponse getStore(Long id) {
    Store store = storeRepository.findById(id)
        .filter(Store::isEmailVerified)
        .orElseThrow(() -> CustomResponseException.resourceNotFoundException("Store not found."));
    return new StoreDetailResponse(store.getId(), store.getName(), store.getCategory(),
        store.getCity(), productService.listForStore(store));
  }
}