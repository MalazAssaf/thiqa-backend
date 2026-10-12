package com.thiqa.backend.thiqa.backend.filter;

import com.thiqa.backend.thiqa.backend.service.JwtService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

  public static final String TOKEN_COOKIE = "thiqa_token";

  private final JwtService jwtService;
  private final UserDetailsService userDetailsService;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    String token = getToken(request);

    if (token != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      try {
        UserDetails user = userDetailsService.loadUserByUsername(jwtService.extractUsername(token));
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user, null,
            user.getAuthorities());
        auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(auth);
      } catch (JwtException | UsernameNotFoundException e) {
        // invalid token or deleted user -> request stays anonymous
      }
    }

    filterChain.doFilter(request, response);
  }

  private String getToken(HttpServletRequest request) {
    if (request.getCookies() != null) {
      for (Cookie cookie : request.getCookies()) {
        if (TOKEN_COOKIE.equals(cookie.getName()))
          return cookie.getValue();
      }
    }
    String header = request.getHeader("Authorization");
    if (header != null && header.startsWith("Bearer "))
      return header.substring(7);
    return null;
  }
}