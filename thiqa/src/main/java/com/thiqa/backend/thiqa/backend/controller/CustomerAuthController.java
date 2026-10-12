package com.thiqa.backend.thiqa.backend.controller;

import com.thiqa.backend.thiqa.backend.dto.CustomerLoginResponse;
import com.thiqa.backend.thiqa.backend.dto.CustomerResponse;
import com.thiqa.backend.thiqa.backend.dto.CustomerSignupRequest;
import com.thiqa.backend.thiqa.backend.dto.LoginRequest;
import com.thiqa.backend.thiqa.backend.service.CustomerAuthService;
import com.thiqa.backend.thiqa.backend.service.JwtService;
import com.thiqa.backend.thiqa.backend.shared.GlobalResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth/customer")
@RequiredArgsConstructor
public class CustomerAuthController {

  private final CustomerAuthService customerAuthService;
  private final JwtService jwtService;

  @PostMapping("/signup")
  public GlobalResponse<CustomerResponse> signup(@Valid @RequestBody CustomerSignupRequest request) {
    return new GlobalResponse<>(customerAuthService.signup(request));
  }

  @PostMapping("/login")
  public ResponseEntity<GlobalResponse<CustomerResponse>> login(@Valid @RequestBody LoginRequest request) {
    CustomerLoginResponse auth = customerAuthService.login(request);
    return ResponseEntity.ok()
        .header(HttpHeaders.SET_COOKIE, jwtService.createCookie(auth.token()).toString())
        .body(new GlobalResponse<>(auth.customer()));
  }
}