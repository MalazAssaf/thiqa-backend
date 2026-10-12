package com.thiqa.backend.thiqa.backend.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.thiqa.backend.thiqa.backend.service.AuthService;
import com.thiqa.backend.thiqa.backend.service.JwtService;
import com.thiqa.backend.thiqa.backend.shared.GlobalResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;
  private final JwtService jwtService;

  @PostMapping("/verify")
  public GlobalResponse<String> verify(@RequestParam String email, @RequestParam String code) {
    authService.verifyEmail(email, code);
    return new GlobalResponse<>("Email verified.");
  }

  @PostMapping("/resend-code")
  public GlobalResponse<String> resendCode(@RequestParam String email) {
    authService.resendCode(email);
    return new GlobalResponse<>("Code sent.");
  }

  @PostMapping("/logout")
  public ResponseEntity<GlobalResponse<String>> logout() {
    return ResponseEntity.ok()
        .header(HttpHeaders.SET_COOKIE, jwtService.clearCookie().toString())
        .body(new GlobalResponse<>("Logged out."));
  }
}