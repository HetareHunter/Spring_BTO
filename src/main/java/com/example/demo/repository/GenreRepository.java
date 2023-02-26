package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Genre;

public interface GenreRepository extends JpaRepository<Genre, Integer>
{
	Optional<Genre> findByName(String name);
}
