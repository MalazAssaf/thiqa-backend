package com.thiqa.backend.thiqa.backend.repository;

import com.thiqa.backend.thiqa.backend.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {
  boolean existsByEmail(String email);

  boolean existsByPhone(String phone);

  Optional<UserAccount> findByEmail(String email);

  Optional<UserAccount> findByPhone(String phone);
}