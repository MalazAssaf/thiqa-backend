package com.thiqa.backend.thiqa.backend.service;

import com.thiqa.backend.thiqa.backend.entity.UserAccount;
import com.thiqa.backend.thiqa.backend.filter.JwtFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.time.Duration;

@Service
public class JwtService {

  @Value("${application.security.jwt.secret-key}")
  private String secretKey;

  @Value("${application.security.jwt.expiration}")
  private long jwtExpiration;

  public String generateToken(UserAccount user) {
    return Jwts.builder()
        .subject(user.getUsername())
        .claim("role", user.getRole().name())
        .issuedAt(new Date())
        .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
        .signWith(getSignInKey())
        .compact();
  }

  public String extractUsername(String token) {
    return extractAllClaims(token).getSubject();
  }

  public String extractRole(String token) {
    return extractAllClaims(token).get("role", String.class);
  }

  public ResponseCookie createCookie(String token) {
    return ResponseCookie.from(JwtFilter.TOKEN_COOKIE, token)
        .httpOnly(true)
        .path("/")
        .maxAge(Duration.ofMillis(jwtExpiration))
        .sameSite("Lax")
        .build();
  }

  public ResponseCookie clearCookie() {
    return ResponseCookie.from(JwtFilter.TOKEN_COOKIE, "")
        .httpOnly(true)
        .path("/")
        .maxAge(0)
        .sameSite("Lax")
        .build();
  }

  private Claims extractAllClaims(String token) {
    return Jwts.parser().verifyWith(getSignInKey()).build().parseSignedClaims(token).getPayload();
  }

  private SecretKey getSignInKey() {
    return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
  }
}