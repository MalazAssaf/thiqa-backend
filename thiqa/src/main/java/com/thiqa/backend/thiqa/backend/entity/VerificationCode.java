package com.thiqa.backend.thiqa.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "verification_codes")
@Getter
@Setter
@NoArgsConstructor
public class VerificationCode {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String code;

  private LocalDateTime expiresAt;

  private boolean used = false;

  @ManyToOne
  @JoinColumn(name = "customer_id")
  private Customer customer;

  // add when the Store entity exists:
  // @ManyToOne
  // @JoinColumn(name = "store_id")
  // private Store store;
}