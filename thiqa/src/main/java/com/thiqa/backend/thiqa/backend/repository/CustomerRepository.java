package com.thiqa.backend.thiqa.backend.repository;

import com.thiqa.backend.thiqa.backend.entity.Customer;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
  boolean existsByEmail(String email);

  boolean existsByPhone(String phone);

  Optional<Customer> findByEmail(String email);

}
