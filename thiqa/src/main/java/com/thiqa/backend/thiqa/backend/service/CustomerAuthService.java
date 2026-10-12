package com.thiqa.backend.thiqa.backend.service;

import com.thiqa.backend.thiqa.backend.dto.CustomerLoginResponse;
import com.thiqa.backend.thiqa.backend.dto.CustomerResponse;
import com.thiqa.backend.thiqa.backend.dto.CustomerSignupRequest;
import com.thiqa.backend.thiqa.backend.dto.LoginRequest;
import com.thiqa.backend.thiqa.backend.entity.Customer;
import com.thiqa.backend.thiqa.backend.entity.Role;
import com.thiqa.backend.thiqa.backend.entity.UserAccount;
import com.thiqa.backend.thiqa.backend.repository.UserAccountRepository;
import com.thiqa.backend.thiqa.backend.shared.CustomResponseException;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerAuthService {

  private final UserAccountRepository userAccountRepository;
  private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
  private final VerificationService verificationService;
  private final JwtService jwtService;

  private CustomerResponse toResponse(Customer c) {
    return new CustomerResponse(c.getId(), c.getFullName(), c.getEmail(),
        c.getPhone(), c.getCity(), c.isEmailVerified());
  }

  public CustomerResponse signup(CustomerSignupRequest request) {
    String email = request.email().trim().toLowerCase();
    String phone = request.phone().trim();

    if (userAccountRepository.existsByEmail(email)) {
      throw CustomResponseException.conflictException("This email is already registered.");
    }
    if (userAccountRepository.existsByPhone(phone)) {
      throw CustomResponseException.conflictException("This phone number is already linked to an account.");
    }

    Customer customer = new Customer();
    customer.setFullName(request.fullName().trim());
    customer.setEmail(email);
    customer.setPhone(phone);
    customer.setCity(request.city());
    customer.setRole(Role.CUSTOMER);
    customer.setPasswordHash(passwordEncoder.encode(request.password()));

    Customer saved = userAccountRepository.save(customer);
    verificationService.sendCode(customer);

    return toResponse(saved);
  }

  public CustomerLoginResponse login(LoginRequest request) {
    UserAccount user = userAccountRepository.findByPhone(request.phone().trim())
        .filter(u -> passwordEncoder.matches(request.password(), u.getPasswordHash()))
        .orElseThrow(() -> CustomResponseException.badCredentials());

    if (!(user instanceof Customer customer)) {
      throw CustomResponseException.badCredentials();
    }
    if (!customer.isEmailVerified()) {
      throw CustomResponseException.forbiddenException("Please verify your email first.");
    }

    return new CustomerLoginResponse(toResponse(customer), jwtService.generateToken(customer));
  }

}