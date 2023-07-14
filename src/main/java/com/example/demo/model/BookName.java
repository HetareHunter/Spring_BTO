package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.sql.Timestamp;
import lombok.Data;

/**
 *書籍名や詳細を管理する。
 */
@Data
@Entity
@Table(name = "BOOK_NAMES")
public class BookName {
  @Id
  // @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private int id;

  @NotNull
  @NotBlank
  @Size(max = 128)
  @Column(name = "title")
  private String title;

  @NotNull
  @NotBlank
  @Size(max = 128)
  @Column(name = "author")
  private String author;

  @NotNull
  @NotBlank
  @Size(max = 255)
  @Column(name = "detail")
  private String detail;

  @NotNull
  @NotBlank
  @Size(max = 128)
  @Column(name = "publisher")
  private String publisher;

  @OneToOne @JoinColumn(name = "genre_id") private Genre genre;

  @Size(max = 255) @Column(name = "img") private String img;

  @NotNull @Column(name = "active") private boolean active;

  @NotNull @Column(name = "created_at") private Timestamp created_at;

  @NotNull @Column(name = "updated_at") private Timestamp updated_at;

  @NotNull @Column(name = "newname") private boolean newName;
}
