package com.alextakayama.auth.dto;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class SignupDTOTests {

  @Test
  public void testSetEmailToLowercase() {
    SignupDTO signupDTO = new SignupDTO();
    signupDTO.setEmail("JOE@DOE.COM");
    assertEquals("joe@doe.com", signupDTO.getEmail());
  }

}
