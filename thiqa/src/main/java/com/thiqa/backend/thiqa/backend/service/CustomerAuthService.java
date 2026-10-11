package com.thiqa.backend.thiqa.backend.service;

import com.thiqa.backend.thiqa.backend.dto.CustomerResponse;
import com.thiqa.backend.thiqa.backend.dto.CustomerSignupRequest;
import com.thiqa.backend.thiqa.backend.entity.Customer;
import com.thiqa.backend.thiqa.backend.repository.CustomerRepository;
import com.thiqa.backend.thiqa.backend.shared.CustomResponseException;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerAuthService {

  private final CustomerRepository customerRepository;
  private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
  private final VerificationService verificationService;

  private CustomerResponse toResponse(Customer c) {
    return new CustomerResponse(c.getId(), c.getFullName(), c.getEmail(),
        c.getPhone(), c.getCity(), c.isEmailVerified(), c.getCreatedAt());
  }

  public CustomerResponse signup(CustomerSignupRequest request) {
    String email = request.email().trim().toLowerCase();
    String phone = request.phone().trim();

    if (customerRepository.existsByEmail(email)) {
      throw CustomResponseException.conflictException("This email is already registered.");
    }
    if (customerRepository.existsByPhone(phone)) {
      throw CustomResponseException.conflictException("This phone number is already linked to an account.");
    }

    Customer customer = new Customer();
    customer.setFullName(request.fullName().trim());
    customer.setEmail(email);
    customer.setPhone(phone);
    customer.setCity(request.city());
    customer.setPasswordHash(passwordEncoder.encode(request.password()));

    Customer saved = customerRepository.save(customer);
    verificationService.sendCode(customer);

    return toResponse(saved);
  }

  public CustomerResponse verifyEmail(String email, String code) {
    Customer customer = customerRepository.findByEmail(email.trim().toLowerCase())
        .orElseThrow(() -> CustomResponseException.resourceNotFoundException("Account not found."));

    if (customer.isEmailVerified()) {
      throw CustomResponseException.conflictException("This email is already verified.");
    }

    verificationService.checkCode(customer, code);
    customer.setEmailVerified(true);
    customerRepository.save(customer);

    return toResponse(customer);
  }

  public void resendCode(String email) {
    Customer customer = customerRepository.findByEmail(email.trim().toLowerCase())
        .orElseThrow(() -> CustomResponseException.resourceNotFoundException("Account not found."));
    if (customer.isEmailVerified()) {
      throw CustomResponseException.conflictException("This email is already verified.");
    }

    verificationService.sendCode(customer);
  }
}