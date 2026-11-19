package com.thiqa.backend.thiqa.backend.service;

import org.springframework.stereotype.Service;

import com.thiqa.backend.thiqa.backend.dto.CustomerResponse;
import com.thiqa.backend.thiqa.backend.entity.Customer;

@Service
public class CustomerService {
  public CustomerResponse me(Customer customer) {
    return new CustomerResponse(customer.getId(), customer.getFullName(), customer.getEmail(), customer.getPhone(),
        customer.getCity(), customer.isEmailVerified());
  }
}
