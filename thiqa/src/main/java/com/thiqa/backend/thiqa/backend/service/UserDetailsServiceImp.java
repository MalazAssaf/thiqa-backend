package com.thiqa.backend.thiqa.backend.service;

import com.thiqa.backend.thiqa.backend.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImp implements UserDetailsService {

  private final UserAccountRepository userAccountRepository;

  @Override
  public UserDetails loadUserByUsername(String id) {
    return userAccountRepository.findById(Long.valueOf(id))
        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
  }
}