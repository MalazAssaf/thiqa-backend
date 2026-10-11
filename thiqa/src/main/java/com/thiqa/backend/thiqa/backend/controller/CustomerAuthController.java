package com.thiqa.backend.thiqa.backend.controller;

import com.thiqa.backend.thiqa.backend.dto.CustomerResponse;
import com.thiqa.backend.thiqa.backend.dto.CustomerSignupRequest;
import com.thiqa.backend.thiqa.backend.service.CustomerAuthService;
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
  public CustomerResponse signup(@Valid @RequestBody CustomerSignupRequest request) {
    return customerAuthService.signup(request);
  }

  @PostMapping("/verify")
  public CustomerResponse verify(@RequestParam String email, @RequestParam String code) {
    return customerAuthService.verifyEmail(email, code);
  }

  @PostMapping("/resend-code")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void resendCode(@RequestParam String email) {
    customerAuthService.resendCode(email);
  }
}