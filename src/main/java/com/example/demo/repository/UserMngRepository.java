package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.model.User;

public interface UserMngRepository extends JpaRepository<User, Integer>
{
	//Optional<User> findByName(String name);
	Optional<User> findByEmail(String email);
	
	@Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.lending WHERE u.id = :id")
    Optional<User> findByIdFetchLending(@Param("id") String id);
}
