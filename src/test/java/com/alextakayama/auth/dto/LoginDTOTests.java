package com.alextakayama.auth.dto;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class LoginDTOTests {

  @Test
  public void testSetEmailToLowercase() {
    LoginDTO loginDTO = new LoginDTO();
    loginDTO.setEmail("JOE@DOE.COM");
    assertEquals("joe@doe.com", loginDTO.getEmail());
  }

}
