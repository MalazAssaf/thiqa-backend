package com.thiqa.backend.thiqa.backend.service;

import com.thiqa.backend.thiqa.backend.entity.Customer;
import com.thiqa.backend.thiqa.backend.entity.VerificationCode;
import com.thiqa.backend.thiqa.backend.repository.VerificationCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class VerificationService {

  private final VerificationCodeRepository codeRepository;
  private final EmailService emailService;

  public void sendCode(Customer customer) {
    String code = String.valueOf(1000 + new Random().nextInt(9000));

    VerificationCode verificationCode = new VerificationCode();
    verificationCode.setCustomer(customer);
    verificationCode.setCode(code);
    verificationCode.setExpiresAt(LocalDateTime.now().plusMinutes(10));
    codeRepository.save(verificationCode);

    emailService.sendVerificationCode(customer.getEmail(), code);
  }

  public void checkCode(Customer customer, String code) {
    VerificationCode verificationCode = codeRepository
        .findTopByCustomerAndUsedFalseOrderByIdDesc(customer)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "No code found."));

    if (verificationCode.getExpiresAt().isBefore(LocalDateTime.now())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The code has expired.");
    }

    if (!verificationCode.getCode().equals(code)) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The code is incorrect.");
    }

    verificationCode.setUsed(true);
    codeRepository.save(verificationCode);
  }
}