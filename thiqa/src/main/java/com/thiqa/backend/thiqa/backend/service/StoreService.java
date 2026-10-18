package com.thiqa.backend.thiqa.backend.service;

import com.thiqa.backend.thiqa.backend.dto.StoreCardResponse;
import com.thiqa.backend.thiqa.backend.entity.City;
import com.thiqa.backend.thiqa.backend.entity.StoreCategory;
import com.thiqa.backend.thiqa.backend.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreService {

  private final StoreRepository storeRepository;

  public List<StoreCardResponse> listStores(StoreCategory category, City city) {
    return storeRepository.findByEmailVerifiedTrueOrderByNameAsc().stream()
        .filter(s -> category == null || s.getCategory() == category)
        .filter(s -> city == null || s.getCity() == city)
        .map(s -> new StoreCardResponse(s.getId(), s.getName(), s.getCategory(), s.getCity()))
        .toList();
  }
}