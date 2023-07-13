package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.sql.Timestamp;
import lombok.Data;

/**
 * 書籍のジャンルを格納する
 */
@Data
@Entity
@Table(name = "GENRE")
public class Genre {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private int id;

  @NotNull @Column(name = "name") private String name;

  @NotNull @Column(name = "created_at") private Timestamp created_at;

  @NotNull @Column(name = "updated_at") private Timestamp updated_at;
}
