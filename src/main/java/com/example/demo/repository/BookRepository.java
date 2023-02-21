package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Book;

public interface BookRepository extends JpaRepository<Book, Integer>
{
	Optional<Book> findById(int id);
}
