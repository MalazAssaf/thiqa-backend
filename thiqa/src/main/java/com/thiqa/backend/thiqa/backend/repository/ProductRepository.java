package com.thiqa.backend.thiqa.backend.repository;

import com.thiqa.backend.thiqa.backend.entity.Product;
import com.thiqa.backend.thiqa.backend.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
  List<Product> findByStoreOrderByNameAsc(Store store);

  Optional<Product> findByIdAndStore(Long id, Store store);
}