package com.thiqa.backend.thiqa.backend.controller;

import com.thiqa.backend.thiqa.backend.dto.CustomerLoginResponse;
import com.thiqa.backend.thiqa.backend.dto.CustomerResponse;
import com.thiqa.backend.thiqa.backend.dto.CustomerSignupRequest;
import com.thiqa.backend.thiqa.backend.dto.LoginRequest;
import com.thiqa.backend.thiqa.backend.filter.JwtFilter;
import com.thiqa.backend.thiqa.backend.service.CustomerAuthService;
import com.thiqa.backend.thiqa.backend.shared.GlobalResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

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
  public GlobalResponse<String> verify(@RequestParam String email, @RequestParam String code) {
    customerAuthService.verifyEmail(email, code);
    return new GlobalResponse<>("Email verified.");
  }

  @PostMapping("/resend-code")
  public GlobalResponse<String> resendCode(@RequestParam String email) {
    customerAuthService.resendCode(email);
    return new GlobalResponse<>("Code sent.");
  }

  @PostMapping("/login")
  public ResponseEntity<GlobalResponse<CustomerResponse>> login(@Valid @RequestBody LoginRequest request) {
    CustomerLoginResponse auth = customerAuthService.login(request);

    ResponseCookie cookie = ResponseCookie.from(JwtFilter.TOKEN_COOKIE, auth.token())
        .httpOnly(true)
        .path("/")
        .maxAge(Duration.ofDays(1))
        .sameSite("Lax")
        .build();

    return ResponseEntity.ok()
        .header(HttpHeaders.SET_COOKIE, cookie.toString())
        .body(new GlobalResponse<>(auth.customer()));
  }
}