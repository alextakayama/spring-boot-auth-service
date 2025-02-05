package com.alextakayama.auth.service;

import com.alextakayama.auth.exception.InvalidCredentialsException;
import com.alextakayama.auth.exception.RecordNotFoundException;
import com.alextakayama.auth.exception.TokenExpiredException;
import com.alextakayama.auth.exception.UserAlreadyExistsException;
import com.alextakayama.auth.model.Token;
import com.alextakayama.auth.model.User;
import com.alextakayama.auth.repository.TokenRepository;
import com.alextakayama.auth.repository.UserRepository;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

  private final JwtTokenService jwtTokenService;

  private final PasswordEncoder passwordEncoder;

  private final TokenRepository tokenRepository;

  private final UserRepository userRepository;

  public Token login(String email, String password) {

    List<User> users = userRepository.findByEmail(email);

    if (users.isEmpty()) {
      throw new RecordNotFoundException("User not found with email: " + email);
    }

    User user = users.get(0);

    if (!passwordEncoder.matches(password, user.getPasswordHash())) {
      throw new InvalidCredentialsException("Invalid credentials");
    }

    JwtTokenResponse tokenResponse = jwtTokenService.generateToken(
        user.getId(), user.getEmail(), user.getName()
    );

    String refreshToken = UUID.randomUUID().toString();

    Token token = Token.builder()
        .accessToken(tokenResponse.getToken())
        .refreshToken(refreshToken)
        .userId(user.getId())
        .userEmail(user.getEmail())
        .expiresAt(tokenResponse.getExpirationDate())
        .build();

    return tokenRepository.save(token);
  }

  public void logout(String refreshToken) {
    tokenRepository.findById(refreshToken).ifPresent(token -> {
      tokenRepository.deleteById(token.getRefreshToken());
    });
  }

  public Token refresh(String refreshToken) {
    Token token = tokenRepository.findById(refreshToken)
        .orElseThrow(() -> new RecordNotFoundException("Refresh Token was not found"));

    User user = userRepository.findById(token.getUserId())
        .orElseThrow(() -> new RecordNotFoundException("User not found with id: " + token.getUserEmail()));

    Date now = new Date();
    if (token.getExpiresAt().after(now)) {
      return token;
    }

    JwtTokenResponse tokenResponse = jwtTokenService.generateToken(
        user.getId(), user.getEmail(), user.getName()
    );

    token.setAccessToken(tokenResponse.getToken());
    token.setExpiresAt(tokenResponse.getExpirationDate());

    return tokenRepository.save(token);
  }

  public User signup(String email, String password, String name) {
    String uuid = UUID.randomUUID().toString();

    if (userRepository.findById(uuid).isPresent()) {
      throw new UserAlreadyExistsException("User with id " + uuid + " already exists");
    }

    if (!userRepository.findByEmail(email).isEmpty()) {
      throw new UserAlreadyExistsException("User with email " + email + " already exists");
    }

    String passwordHash = passwordEncoder.encode(password);

    User user = User.builder()
        .id(uuid)
        .email(email)
        .name(name)
        .passwordHash(passwordHash)
        .build();

    return userRepository.save(user);
  }

  public Token validateToken(String accessToken) {
    List<Token> tokens = tokenRepository.findByAccessToken(accessToken);

    if (tokens.isEmpty()) {
      throw new RecordNotFoundException("Token not found");
    }

    Token token = tokens.get(0);

    Date now = new Date();
    if (token.getExpiresAt().before(now)) {
      throw new TokenExpiredException("Token is expired");
    }

    return token;
  }

}
