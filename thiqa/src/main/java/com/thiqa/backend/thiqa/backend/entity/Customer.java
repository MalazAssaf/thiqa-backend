package com.thiqa.backend.thiqa.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "customers")
@Getter
@Setter
@NoArgsConstructor
public class Customer extends UserAccount {

  @Column(nullable = false)
  private String fullName;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private City city;
}