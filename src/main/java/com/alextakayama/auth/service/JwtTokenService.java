package com.alextakayama.auth.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtTokenService {

  @Value("${app.jwtSecretKey}")
  private String jwtSecretKey;

  @Value("${app.jwtExpirationInMs}")
  private int jwtExpirationInMs;

  public JwtTokenResponse generateToken(String userId, String userEmail, String userName) {
    Date now = new Date();
    Date expirationDate = new Date(now.getTime() + jwtExpirationInMs);

    Map<String, String> claims = new HashMap<>();
    claims.put("userId", userId);
    claims.put("userEmail", userEmail);
    claims.put("userName", userName);

    String token = Jwts.builder()
        .claims(claims)
        .subject(userId)
        .issuedAt(now)
        .expiration(expirationDate)
        .signWith(getSigningKey())
        .compact();

    return new JwtTokenResponse(token, expirationDate);
  }

  private Key getSigningKey() {
    byte[] keyBytes = jwtSecretKey.getBytes(StandardCharsets.UTF_8);
    return Keys.hmacShaKeyFor(keyBytes);
  }

}
