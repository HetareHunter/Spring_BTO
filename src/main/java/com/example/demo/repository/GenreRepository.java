package com.example.demo.repository;

import com.example.demo.model.Genre;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface GenreRepository extends JpaRepository<Genre, Integer> {
  Optional<Genre> findByName(String name);

  @Transactional
  @Modifying
  @Query("UPDATE Genre g SET g.name = :name, g.updated_at = CURRENT_TIMESTAMP WHERE g.id = :id")
  int setQueryName(@Param("name") String name, @Param("id") int id);
}
