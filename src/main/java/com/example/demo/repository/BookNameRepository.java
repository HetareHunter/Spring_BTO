package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.BookName;

public interface BookNameRepository extends JpaRepository<BookName, Integer>
{
	Optional<BookName> findByTitle(String title);
}
