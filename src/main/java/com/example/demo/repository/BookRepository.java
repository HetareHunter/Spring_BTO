package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Book;
import com.example.demo.model.BookName;

public interface BookRepository extends JpaRepository<Book, Integer>
{
	Optional<Book> findById(int id);
	Optional<Book> findByBookNameId(Optional<BookName> bookNameId);
}
