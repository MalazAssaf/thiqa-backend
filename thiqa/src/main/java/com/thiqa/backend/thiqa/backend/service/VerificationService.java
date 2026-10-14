package com.thiqa.backend.thiqa.backend.service;

import com.thiqa.backend.thiqa.backend.entity.UserAccount;
import com.thiqa.backend.thiqa.backend.entity.VerificationCode;
import com.thiqa.backend.thiqa.backend.repository.VerificationCodeRepository;
import com.thiqa.backend.thiqa.backend.shared.CustomResponseException;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class VerificationService {

  private final VerificationCodeRepository verificationCodeRepository;
  private final EmailService emailService;

  public void sendCode(UserAccount user) {
    String code = String.valueOf(1000 + new Random().nextInt(9000));

    VerificationCode verificationCode = new VerificationCode();
    verificationCode.setUser(user);
    verificationCode.setCode(code);
    verificationCode.setExpiresAt(LocalDateTime.now().plusMinutes(10));
    verificationCodeRepository.save(verificationCode);

    emailService.sendVerificationCode(user.getEmail(), code);
  }

  public void checkCode(UserAccount user, String code) {
    VerificationCode verificationCode = verificationCodeRepository.findTopByUserAndUsedFalseOrderByIdDesc(user)
        .orElseThrow(() -> CustomResponseException.badRequestException("No code found. Please request a new one."));

    if (verificationCode.getExpiresAt().isBefore(LocalDateTime.now())) {
      throw CustomResponseException.badRequestException("The code has expired. Please request a new one.");
    }

    if (!verificationCode.getCode().equals(code)) {
      throw CustomResponseException.badRequestException("The code is incorrect.");
    }

    verificationCode.setUsed(true);
    verificationCodeRepository.save(verificationCode);
  }
}