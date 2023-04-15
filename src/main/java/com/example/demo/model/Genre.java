package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.sql.Timestamp;
import lombok.Data;

/**
 * @author umaib
 *書籍の貸し出し状況を管理する。
 */
@Data
@Entity
// @SequenceGenerator(name = "GENRE_GENERATOR", sequenceName = "genreSeq",
// allocationSize = 1)
@Table(name = "GENRE")
public class Genre {
  @Id
  //@GeneratedValue(strategy = GenerationType.IDENTITY, generator =
  //"GENRE_GENERATOR")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private int id;

  @Column(name = "name") private String name;

  @Column(name = "created_at") private Timestamp created_at;

  @Column(name = "updated_at") private Timestamp updated_at;
}
