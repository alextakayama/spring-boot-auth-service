package com.alextakayama.auth.model;

import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

/**
 * @author Alex Takayama <alex.takayama@gmail.com>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@RedisHash("Token")
public class Token implements Serializable {

  @Id
  private String refreshToken;

  @Indexed
  private String accessToken;

  private String userId;

  private String userEmail;

  private Date expiresAt;

}
