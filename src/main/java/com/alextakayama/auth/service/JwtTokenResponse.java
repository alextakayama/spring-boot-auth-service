package com.alextakayama.auth.service;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JwtTokenResponse {

  private final String token;

  private final Date expirationDate;

}
