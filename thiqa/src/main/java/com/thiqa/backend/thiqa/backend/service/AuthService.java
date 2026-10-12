package com.thiqa.backend.thiqa.backend.service;

import org.springframework.stereotype.Service;

import com.thiqa.backend.thiqa.backend.entity.UserAccount;
import com.thiqa.backend.thiqa.backend.repository.UserAccountRepository;
import com.thiqa.backend.thiqa.backend.shared.CustomResponseException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final UserAccountRepository userAccountRepository;
  private final VerificationService verificationService;

  public void verifyEmail(String email, String code) {
    UserAccount user = findByEmail(email);
    if (user.isEmailVerified()) {
      throw CustomResponseException.conflictException("This email is already verified.");
    }
    verificationService.checkCode(user, code);
    user.setEmailVerified(true);
    userAccountRepository.save(user);
  }

  public void resendCode(String email) {
    UserAccount user = findByEmail(email);
    if (user.isEmailVerified()) {
      throw CustomResponseException.conflictException("This email is already verified.");
    }
    verificationService.sendCode(user);
  }

  private UserAccount findByEmail(String email) {
    return userAccountRepository.findByEmail(email.trim().toLowerCase())
        .orElseThrow(() -> CustomResponseException.resourceNotFoundException("Account not found."));
  }
}
