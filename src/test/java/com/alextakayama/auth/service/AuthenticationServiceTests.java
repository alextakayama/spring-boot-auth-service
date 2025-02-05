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
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTests {

  private static final String ACCESS_TOKEN = "accessToken";
  private static final String JWT_TOKEN = "JwtToken";
  private static final String REFRESH_TOKEN = "refreshToken";
  private static final String USER_EMAIL = "joe@doe.com";
  private static final String USER_ID = "00000000-0000-0000-0000-000000000000";
  private static final String USER_NAME = "Joe Doe";
  private static final String USER_PASSWORD = "pass1234";
  private static final String USER_PASSWORD_HASH = "hash1234";

  @Mock
  private JwtTokenService jwtTokenService;

  @Mock
  private PasswordEncoder passwordEncoder;

  @Mock
  private TokenRepository tokenRepository;

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private AuthenticationService authenticationService;

  private User buildUser() {
    return User.builder()
        .email(USER_EMAIL)
        .id(USER_ID)
        .name(USER_EMAIL)
        .passwordHash(USER_PASSWORD_HASH)
        .build();
  }

  private Token buildToken(Date expiresAt) {
    return Token.builder()
        .accessToken(ACCESS_TOKEN)
        .expiresAt(expiresAt)
        .refreshToken(REFRESH_TOKEN)
        .userEmail(USER_EMAIL)
        .userId(USER_ID)
        .build();
  }

  private JwtTokenResponse buildJwtTokenResponse(Date expiresAt) {
    return new JwtTokenResponse(JWT_TOKEN, expiresAt);
  }

  private Date dateAfterHours(int hours) {
    return new Date(System.currentTimeMillis() + (hours * 3600_000L));
  }

  private Date dateBeforeHours(int hours) {
    return new Date(System.currentTimeMillis() - (hours * 3600_000L));
  }

  @Test
  public void testLoginWithSuccess() {
    User user = buildUser();

    when(userRepository.findByEmail(USER_EMAIL)).thenReturn(List.of(user));

    when(passwordEncoder.matches(USER_PASSWORD, USER_PASSWORD_HASH)).thenReturn(true);

    JwtTokenResponse tokenResponse = buildJwtTokenResponse(new Date());

    when(jwtTokenService.generateToken(USER_ID, USER_EMAIL, USER_NAME)).thenReturn(tokenResponse);

    Token expectedToken = buildToken(new Date());

    when(tokenRepository.save(any(Token.class))).thenReturn(expectedToken);

    Token actualToken = authenticationService.login(USER_EMAIL, USER_PASSWORD);

    assertEquals(expectedToken, actualToken);
  }

  @Test
  public void testLoginEmailNotFound() {
    when(userRepository.findByEmail(USER_EMAIL)).thenReturn(List.of());

    Exception exception = assertThrows(RecordNotFoundException.class, () -> {
      authenticationService.login(USER_EMAIL, USER_PASSWORD);
    });

    assertTrue(exception.getMessage().contains("User not found with email"));
  }

  @Test
  public void testLoginInvalidPassword() {
    User user = buildUser();

    when(userRepository.findByEmail(USER_EMAIL)).thenReturn(List.of(user));

    when(passwordEncoder.matches(USER_PASSWORD, USER_PASSWORD_HASH)).thenReturn(false);

    Exception exception = assertThrows(InvalidCredentialsException.class, () -> {
      authenticationService.login(USER_EMAIL, USER_PASSWORD);
    });

    assertTrue(exception.getMessage().contains("Invalid credentials"));
  }

  @Test
  public void testLogoutTokenFound() {
    Token token = buildToken(dateAfterHours(1));

    when(tokenRepository.findById(REFRESH_TOKEN)).thenReturn(Optional.of(token));

    authenticationService.logout(REFRESH_TOKEN);

    verify(tokenRepository).findById(REFRESH_TOKEN);
    verify(tokenRepository).deleteById(REFRESH_TOKEN);
  }

  @Test
  public void testLogoutTokenNotFound() {
    when(tokenRepository.findById(REFRESH_TOKEN)).thenReturn(Optional.empty());

    authenticationService.logout(REFRESH_TOKEN);

    verify(tokenRepository).findById(REFRESH_TOKEN);
    verify(tokenRepository, never()).deleteById(REFRESH_TOKEN);
  }

  @Test
  public void testRefreshExpiredToken() {
    Date now = new Date();
    Date expiredAt = dateBeforeHours(1);

    Token expiredToken = buildToken(expiredAt);
    when(tokenRepository.findById(REFRESH_TOKEN)).thenReturn(Optional.of(expiredToken));

    User user = buildUser();
    when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));

    JwtTokenResponse tokenResponse = buildJwtTokenResponse(dateAfterHours(1));
    when(jwtTokenService.generateToken(USER_ID, USER_EMAIL, USER_NAME)).thenReturn(tokenResponse);

    when(tokenRepository.save(any(Token.class))).thenAnswer(invocation -> invocation.getArgument(0));

    Token refreshedToken = authenticationService.refresh(REFRESH_TOKEN);

    assertEquals(REFRESH_TOKEN, refreshedToken.getRefreshToken());
    assertEquals(USER_EMAIL, refreshedToken.getUserEmail());
    assertEquals(USER_ID, refreshedToken.getUserId());
    assertEquals(JWT_TOKEN, refreshedToken.getAccessToken());
    assertTrue(now.before(refreshedToken.getExpiresAt()));
  }

  @Test
  public void testRefreshNonExpiredToken() {
    Date now = new Date();
    Date expiresAt = dateAfterHours(1);

    Token nonExpiredToken = buildToken(expiresAt);
    when(tokenRepository.findById(REFRESH_TOKEN)).thenReturn(Optional.of(nonExpiredToken));

    User user = buildUser();
    when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));

    Token refreshedToken = authenticationService.refresh(REFRESH_TOKEN);

    assertEquals(nonExpiredToken, refreshedToken);
  }

  @Test
  public void testRefreshTokenNotFound() {
    when(tokenRepository.findById(REFRESH_TOKEN)).thenReturn(Optional.empty());

    Exception exception = assertThrows(RecordNotFoundException.class, () -> {
      authenticationService.refresh(REFRESH_TOKEN);
    });

    assertTrue(exception.getMessage().contains("Refresh Token was not found"));
  }

  @Test
  public void testRefreshUserNotFound() {
    Date now = new Date();
    Date expiresAt = dateAfterHours(1);

    Token nonExpiredToken = buildToken(expiresAt);
    when(tokenRepository.findById(REFRESH_TOKEN)).thenReturn(Optional.of(nonExpiredToken));

    when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());

    Exception exception = assertThrows(RecordNotFoundException.class, () -> {
      authenticationService.refresh(REFRESH_TOKEN);
    });

    assertTrue(exception.getMessage().contains("User not found with id"));
  }

  @Test
  public void testSignupSuccessfully() {
    when(userRepository.findById(any(String.class))).thenReturn(Optional.empty());

    when(userRepository.findByEmail(USER_EMAIL)).thenReturn(List.of());

    when(passwordEncoder.encode(USER_PASSWORD)).thenReturn(USER_PASSWORD_HASH);

    when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

    User user = authenticationService.signup(USER_EMAIL, USER_PASSWORD, USER_NAME);

    assertDoesNotThrow(() -> UUID.fromString(user.getId()));
    assertEquals(USER_EMAIL, user.getEmail());
    assertEquals(USER_NAME, user.getName());
    assertEquals(USER_PASSWORD_HASH, user.getPasswordHash());
  }

  @Test
  public void testSignupUUIDAlreadyExists() {
    User existingUser = buildUser();
    when(userRepository.findById(any(String.class))).thenReturn(Optional.of(existingUser));

    Exception exception = assertThrows(UserAlreadyExistsException.class, () -> {
      authenticationService.signup(USER_EMAIL, USER_PASSWORD, USER_NAME);
    });

    assertTrue(exception.getMessage().contains("already exists"));
  }

  @Test
  public void testSignupEmailAlreadyExists() {
    when(userRepository.findById(any(String.class))).thenReturn(Optional.empty());

    User user = buildUser();
    when(userRepository.findByEmail(any(String.class))).thenReturn(List.of(user));

    Exception exception = assertThrows(UserAlreadyExistsException.class, () -> {
      authenticationService.signup(USER_EMAIL, USER_PASSWORD, USER_NAME);
    });

    assertTrue(exception.getMessage().contains("already exists"));
  }

  @Test
  public void testValidateNonExpiredToken() {
    Date now = new Date();
    Date expiresAt = dateAfterHours(1);

    Token nonExpiredToken = buildToken(expiresAt);

    when(tokenRepository.findByAccessToken(ACCESS_TOKEN)).thenReturn(List.of(nonExpiredToken));

    Token actualToken = authenticationService.validateToken(ACCESS_TOKEN);

    assertEquals(nonExpiredToken, actualToken);
  }

  @Test
  public void testValidateTokenNotFound() {
    when(tokenRepository.findByAccessToken(ACCESS_TOKEN)).thenReturn(List.of());

    Exception exception = assertThrows(RecordNotFoundException.class, () -> {
      authenticationService.validateToken(ACCESS_TOKEN);
    });

    assertTrue(exception.getMessage().contains("Token not found"));
  }

  @Test
  public void testValidateExpiredToken() {
    Date now = new Date();
    Date expiredAt = dateBeforeHours(1);

    Token expiredToken = buildToken(expiredAt);
    when(tokenRepository.findByAccessToken(ACCESS_TOKEN)).thenReturn(List.of(expiredToken));

    Exception exception = assertThrows(TokenExpiredException.class, () -> {
      authenticationService.validateToken(ACCESS_TOKEN);
    });

    assertTrue(exception.getMessage().contains("Token is expired"));
  }

}
