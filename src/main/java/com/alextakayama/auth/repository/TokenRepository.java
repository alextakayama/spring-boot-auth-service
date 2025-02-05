package com.alextakayama.auth.repository;

import com.alextakayama.auth.model.Token;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface TokenRepository extends CrudRepository<Token, String> {
  List <Token> findByAccessToken(String accessToken);
}
