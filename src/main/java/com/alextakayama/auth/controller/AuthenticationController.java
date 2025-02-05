package com.alextakayama.auth.controller;

import com.alextakayama.auth.dto.LoginDTO;
import com.alextakayama.auth.dto.LogoutDTO;
import com.alextakayama.auth.dto.RefreshDTO;
import com.alextakayama.auth.dto.SignupDTO;
import com.alextakayama.auth.dto.ValidateTokenDTO;
import com.alextakayama.auth.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authentication")
@RequiredArgsConstructor
public class AuthenticationController {

  private final AuthenticationService authenticationService;

  @PostMapping("/signup")
  public ResponseEntity<?> signup(@Valid @RequestBody SignupDTO signupInfo) {
    var user = authenticationService.signup(
        signupInfo.getEmail(), signupInfo.getPassword(), signupInfo.getPassword()
    );
    return ResponseEntity.status(HttpStatus.CREATED).body(user);
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@Valid @RequestBody LoginDTO loginInfo) {
    var token = authenticationService.login(
        loginInfo.getEmail(), loginInfo.getPassword()
    );
    return ResponseEntity.ok(token);
  }

  @PostMapping("/validate-token")
  public ResponseEntity<?> validateToken(@Valid @RequestBody ValidateTokenDTO validateTokenInfo) {
    var token = authenticationService.validateToken(validateTokenInfo.getAccessToken());
    return ResponseEntity.ok(token);
  }

  @PostMapping("/refresh")
  public ResponseEntity<?> logout(@Valid @RequestBody RefreshDTO refreshInfo) {
    var token = authenticationService.refresh(refreshInfo.getRefreshToken());
    return ResponseEntity.ok(token);
  }

  @PostMapping("/logout")
  public ResponseEntity<?> logout(@Valid @RequestBody LogoutDTO logoutInfo) {
    authenticationService.logout(logoutInfo.getRefreshToken());
    return ResponseEntity.noContent().build();
  }
}
