package com.alextakayama.auth.repository;

import com.alextakayama.auth.model.User;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, String> {
  List <User> findByEmail(String email);
}
