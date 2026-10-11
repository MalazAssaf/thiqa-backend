package com.thiqa.backend.thiqa.backend.controller;

import com.thiqa.backend.thiqa.backend.dto.CustomerResponse;
import com.thiqa.backend.thiqa.backend.dto.CustomerSignupRequest;
import com.thiqa.backend.thiqa.backend.service.CustomerAuthService;
import com.thiqa.backend.thiqa.backend.shared.GlobalResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth/customer")
@RequiredArgsConstructor
public class CustomerAuthController {

  private final CustomerAuthService customerAuthService;

  @PostMapping("/signup")
  @ResponseStatus(HttpStatus.CREATED)
  public GlobalResponse<CustomerResponse> signup(@Valid @RequestBody CustomerSignupRequest request) {
    return new GlobalResponse<>(customerAuthService.signup(request));
  }

  @PostMapping("/verify")
  public GlobalResponse<CustomerResponse> verify(@RequestParam String email, @RequestParam String code) {
    return new GlobalResponse<>(customerAuthService.verifyEmail(email, code));
  }

  @PostMapping("/resend-code")
  public GlobalResponse<String> resendCode(@RequestParam String email) {
    customerAuthService.resendCode(email);
    return new GlobalResponse<>("Code sent.");
  }
}