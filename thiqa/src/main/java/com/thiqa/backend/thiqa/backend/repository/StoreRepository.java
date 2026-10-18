package com.thiqa.backend.thiqa.backend.repository;

import com.thiqa.backend.thiqa.backend.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Long> {
  List<Store> findByEmailVerifiedTrueOrderByNameAsc();
}