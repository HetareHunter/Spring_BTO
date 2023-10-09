package com.example.demo.repository;

import com.example.demo.model.Book;
import com.example.demo.model.BookName;
import com.example.demo.util.BookState;
import java.util.ArrayList;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Integer> {
  Optional<Book> findById(int id);
  ArrayList<Book> findByBookNameId(BookName bookNameId);
  ArrayList<Book> findByBookNameIdOrderByIdAsc(BookName bookNameId);
  boolean existsByBookNameId(BookName bookNameId);
  Optional<Book> findTopByBookNameIdOrderByIdAsc(BookName bookNameId);
  Optional<Book> findTopByBookNameIdOrderByIdDesc(BookName bookNameId);
  ArrayList<Book> findByState(BookState state);
  Optional<Book> findTopByBookNameIdAndState(BookName bookNameId,
                                             BookState state);
}
