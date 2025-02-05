package com.alextakayama.auth.controller;

import com.alextakayama.auth.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

  private final AdminService adminService;

  @GetMapping("/users")
  public ResponseEntity<?> listUsers() {
    return ResponseEntity.ok(adminService.getAllUsers());
  }

  @DeleteMapping("/users/{id}")
  public ResponseEntity<?> deleteUser(@PathVariable String id) {
    adminService.deleteUser(id);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/tokens")
  public ResponseEntity<?> listTokens() {
    return ResponseEntity.ok(adminService.getAllTokens());
  }

  @DeleteMapping("/tokens/{id}")
  public ResponseEntity<?> deleteToken(@PathVariable String id) {
    adminService.deleteToken(id);
    return ResponseEntity.noContent().build();
  }

}
