package com.thiqa.backend.thiqa.backend.service;

import com.thiqa.backend.thiqa.backend.dto.CustomerResponse;
import com.thiqa.backend.thiqa.backend.dto.CustomerSignupRequest;
import com.thiqa.backend.thiqa.backend.entity.Customer;
import com.thiqa.backend.thiqa.backend.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class CustomerAuthService {

  private final CustomerRepository customerRepository;
  private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
  private final VerificationService verificationService;

  public CustomerResponse signup(CustomerSignupRequest request) {
    String email = request.email().trim().toLowerCase();
    String phone = request.phone().trim();

    if (customerRepository.existsByEmail(email)) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "This email is already registered.");
    }
    if (customerRepository.existsByPhone(phone)) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "This phone number is already linked to an account.");
    }

    Customer customer = new Customer();
    customer.setFullName(request.fullName().trim());
    customer.setEmail(email);
    customer.setPhone(phone);
    customer.setCity(request.city());
    customer.setPasswordHash(passwordEncoder.encode(request.password()));

    Customer saved = customerRepository.save(customer);
    verificationService.sendCode(customer);

    return new CustomerResponse(
        saved.getId(),
        saved.getFullName(),
        saved.getEmail(),
        saved.getPhone(),
        saved.getCity(),
        saved.isEmailVerified(),
        saved.getCreatedAt());
  }

  public CustomerResponse verifyEmail(String email, String code) {
    Customer customer = customerRepository.findByEmail(email.trim().toLowerCase())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found."));

    if (customer.isEmailVerified()) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "This email is already verified.");
    }

    verificationService.checkCode(customer, code);
    customer.setEmailVerified(true);
    customerRepository.save(customer);

    return new CustomerResponse(customer.getId(), customer.getFullName(), customer.getEmail(),
        customer.getPhone(), customer.getCity(), customer.isEmailVerified(), customer.getCreatedAt());
  }

  public void resendCode(String email) {
    Customer customer = customerRepository.findByEmail(email.trim().toLowerCase())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found."));
    if (customer.isEmailVerified()) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "This email is already verified.");
    }

    verificationService.sendCode(customer);
  }
}