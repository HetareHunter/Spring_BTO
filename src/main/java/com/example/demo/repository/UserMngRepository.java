package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.User;

public interface UserMngRepository extends JpaRepository<User, Long>
{
	Optional<User> findByName(String name);
}
