package com.alextakayama.auth.service;

import com.alextakayama.auth.model.Token;
import com.alextakayama.auth.model.User;
import com.alextakayama.auth.repository.TokenRepository;
import com.alextakayama.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {

  private final TokenRepository tokenRepository;

  private final UserRepository userRepository;

  public void deleteUser(String userId) {
    userRepository.deleteById(userId);
  }

  public void deleteToken(String tokenId) {
    tokenRepository.deleteById(tokenId);
  }

  public Iterable<User> getAllUsers() {
    return userRepository.findAll();
  }

  public Iterable<Token> getAllTokens() {
    return tokenRepository.findAll();
  }

}
