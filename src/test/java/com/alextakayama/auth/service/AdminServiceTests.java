package com.alextakayama.auth.service;

import com.alextakayama.auth.model.Token;
import com.alextakayama.auth.model.User;
import com.alextakayama.auth.repository.TokenRepository;
import com.alextakayama.auth.repository.UserRepository;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AdminServiceTests {

  private static final String ACCESS_TOKEN = "accessToken";
  private static final String REFRESH_TOKEN = "refreshToken";
  private static final String USER_EMAIL = "joe@doe.com";
  private static final String USER_ID = "userId";
  private static final String USER_NAME = "Joe Doe";
  private static final String USER_PASSWORD_HASH = "hash1234";

  @Mock
  private TokenRepository tokenRepository;

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private AdminService adminService;

  private User buildUser(String userId) {
    return User.builder()
        .email(USER_EMAIL)
        .id(userId)
        .name(USER_NAME)
        .passwordHash(USER_PASSWORD_HASH)
        .build();
  }

  private Token buildToken(String refreshToken) {
    return Token.builder()
        .accessToken(ACCESS_TOKEN)
        .expiresAt(new Date())
        .refreshToken(refreshToken)
        .userEmail(USER_EMAIL)
        .userId(USER_ID)
        .build();
  }

  @Test
  public void testDeleteUser() {;
    adminService.deleteUser(USER_ID);
    verify(userRepository).deleteById(USER_ID);
  }

  @Test
  public void testDeleteToken() {
    adminService.deleteToken(REFRESH_TOKEN);
    verify(tokenRepository).deleteById(REFRESH_TOKEN);
  }

  @Test
  public void testGetAllUsers() {
    User user1 = buildUser("1" + USER_ID);
    User user2 = buildUser("2" + USER_ID);

    List<User> mockUsers = List.of(user1, user2);

    when(userRepository.findAll()).thenReturn(mockUsers);

    Iterable<User> actualUsers = adminService.getAllUsers();

    assertEquals(mockUsers, actualUsers);
  }

  @Test
  public void testGetAllTokens() {
    Token token1 = buildToken("1" + REFRESH_TOKEN);
    Token token2 = buildToken("2" + REFRESH_TOKEN);

    List<Token> mockTokens = List.of(token1, token2);

    when(tokenRepository.findAll()).thenReturn(mockTokens);

    Iterable<Token> actualTokens = adminService.getAllTokens();

    assertEquals(mockTokens, actualTokens);
  }

}
