package com.thiqa.backend.thiqa.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "customers")
@Getter
@Setter
@NoArgsConstructor
public class Customer {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String fullName;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false, unique = true)
  private String phone;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private City city;

  @Column(nullable = false)
  private String passwordHash;

  @Column(nullable = false)
  private boolean emailVerified = false;

  @CreationTimestamp
  @Column(nullable = false)
  private LocalDateTime createdAt;
}