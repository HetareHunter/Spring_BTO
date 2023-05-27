package com.example.demo.repository;

import com.example.demo.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserMngRepository extends JpaRepository<User, Integer> {
  Optional<User> findByEmail(String email);
}
