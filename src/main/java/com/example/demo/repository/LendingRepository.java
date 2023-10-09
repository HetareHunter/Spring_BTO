package com.example.demo.repository;

import com.example.demo.model.Book;
import com.example.demo.model.Lending;
import com.example.demo.model.User;
import com.example.demo.util.LendingState;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LendingRepository extends JpaRepository<Lending, Integer> {
  Optional<Lending> findById(int id);
  Optional<Lending> findByBook(Book book);
  Optional<Lending> findByBookAndState(Book book, LendingState state);
  Optional<Lending> findByUser(User user);
  boolean existsByBook(Book book);
  ArrayList<Lending> findListByUser(User user);
  List<Lending> findListByState(LendingState state);
  List<Lending> findListByUserAndState(User user, LendingState state);
}
