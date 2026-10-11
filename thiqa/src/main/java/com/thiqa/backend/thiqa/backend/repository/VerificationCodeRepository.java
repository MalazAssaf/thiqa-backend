package com.thiqa.backend.thiqa.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thiqa.backend.thiqa.backend.entity.Customer;
import com.thiqa.backend.thiqa.backend.entity.VerificationCode;

public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long> {
  Optional<VerificationCode> findTopByCustomerAndUsedFalseOrderByIdDesc(Customer customer);
}
