package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Lending;

public interface LendingRepository extends JpaRepository<Lending, Integer>
{
	Optional<Lending> findById(int id);
}
