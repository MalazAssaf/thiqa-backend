package com.thiqa.backend.thiqa.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class EmailService {

  private final JavaMailSender mailSender;

  @Value("${spring.mail.username}")
  private String from;

  public void sendVerificationCode(String to, String code) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setFrom("Thiqa <" + from + ">");
    message.setTo(to);
    message.setSubject("Your Thiqa verification code");
    message.setText(
        "Your Thiqa verification code is: " + code + "\n\n" +
            "It expires in 10 minutes.\n" +
            "If you didn't create a Thiqa account, you can ignore this email.");

    try {
      mailSender.send(message);
    } catch (MailException e) {
      throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
          "We couldn't send the verification email. Please try again.");
    }
  }
}