package com.example.demo.repository;

import com.example.demo.model.BookName;
import java.util.ArrayList;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookNameRepository extends JpaRepository<BookName, Integer> {
  Optional<BookName> findByTitle(String title);
  //部分一致でタイトル検索をする。降順
  ArrayList<BookName> findByTitleLikeOrderByTitle(String title);
  ArrayList<BookName> findAllByOrderByTitle();
}
