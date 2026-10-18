package com.thiqa.backend.thiqa.backend.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.thiqa.backend.thiqa.backend.dto.LoginRequest;
import com.thiqa.backend.thiqa.backend.dto.StoreLoginResponse;
import com.thiqa.backend.thiqa.backend.dto.StoreResponse;
import com.thiqa.backend.thiqa.backend.dto.StoreSignupRequest;
import com.thiqa.backend.thiqa.backend.entity.Role;
import com.thiqa.backend.thiqa.backend.entity.Store;
import com.thiqa.backend.thiqa.backend.entity.UserAccount;
import com.thiqa.backend.thiqa.backend.repository.UserAccountRepository;
import com.thiqa.backend.thiqa.backend.shared.CustomResponseException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StoreAuthService {

  private final UserAccountRepository userAccountRepository;
  private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
  private final VerificationService verificationService;
  private final JwtService jwtService;

  private StoreResponse toResponse(Store s) {
    return new StoreResponse(s.getId(), s.getName(), s.getCategory(), s.getCity(),
        s.getEmail(), s.getPhone(), s.isEmailVerified(), s.isAutoReject(), s.getRejectThreshold());
  }

  public StoreResponse signup(StoreSignupRequest request) {
    String email = request.email().trim().toLowerCase();
    String phone = request.phone().trim();

    if (userAccountRepository.existsByEmail(email)) {
      throw CustomResponseException.conflictException("This email is already registered.");
    }
    if (userAccountRepository.existsByPhone(phone)) {
      throw CustomResponseException.conflictException("This phone number is already linked to an account.");
    }

    Store store = new Store();
    store.setName(request.name().trim());
    store.setCategory(request.category());
    store.setCity(request.city());
    store.setEmail(email);
    store.setPhone(phone);
    store.setRole(Role.STORE);
    store.setPasswordHash(passwordEncoder.encode(request.password()));

    if (Boolean.TRUE.equals(request.autoReject())) {
      store.setAutoReject(true);
      if (request.rejectThreshold() != null) {
        store.setRejectThreshold(request.rejectThreshold());
      }
    }

    Store saved = userAccountRepository.save(store);
    verificationService.sendCode(saved);

    return toResponse(saved);
  }

  public StoreLoginResponse login(LoginRequest request) {
    UserAccount user = userAccountRepository.findByPhone(request.phone().trim())
        .filter(u -> passwordEncoder.matches(request.password(), u.getPasswordHash()))
        .orElseThrow(() -> CustomResponseException.badCredentials());

    if (!(user instanceof Store store)) {
      throw CustomResponseException.badCredentials();
    }

    if (!store.isEmailVerified()) {
      verificationService.sendCode(store);
      return new StoreLoginResponse(toResponse(store), null);
    }

    return new StoreLoginResponse(toResponse(store), jwtService.generateToken(store));
  }
}
