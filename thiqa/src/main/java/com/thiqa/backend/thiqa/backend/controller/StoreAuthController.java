package com.thiqa.backend.thiqa.backend.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.thiqa.backend.thiqa.backend.dto.LoginRequest;
import com.thiqa.backend.thiqa.backend.dto.StoreLoginResponse;
import com.thiqa.backend.thiqa.backend.dto.StoreResponse;
import com.thiqa.backend.thiqa.backend.dto.StoreSignupRequest;
import com.thiqa.backend.thiqa.backend.service.JwtService;
import com.thiqa.backend.thiqa.backend.service.StoreAuthService;
import com.thiqa.backend.thiqa.backend.shared.GlobalResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth/store")
@RequiredArgsConstructor
public class StoreAuthController {

  private final StoreAuthService storeAuthService;
  private final JwtService jwtService;

  @PostMapping("/signup")
  public GlobalResponse<StoreResponse> signup(@Valid @RequestBody StoreSignupRequest request) {
    return new GlobalResponse<>(storeAuthService.signup(request));
  }

  @PostMapping("/login")
  public ResponseEntity<GlobalResponse<StoreResponse>> login(@Valid @RequestBody LoginRequest request) {
    StoreLoginResponse auth = storeAuthService.login(request);
    return ResponseEntity.ok()
        .header(HttpHeaders.SET_COOKIE, jwtService.createCookie(auth.token()).toString())
        .body(new GlobalResponse<>(auth.store()));
  }
}
