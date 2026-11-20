package com.thiqa.backend.thiqa.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "addresses")
@Getter
@Setter
@NoArgsConstructor
public class Address {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "customer_id")
  private Customer customer;

  @Column(nullable = false)
  private String label;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private City city;

  @Column(nullable = false)
  private String details;

  private boolean defaultAddress = false;
}