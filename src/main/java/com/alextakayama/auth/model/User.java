package com.alextakayama.auth.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
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
@RedisHash("User")
public class User implements Serializable {

  @Id
  private String id;

  @Indexed
  private String email;

  private String name;

  @JsonIgnore
  private String passwordHash;

}
