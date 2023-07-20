package com.example.demo.model;

import com.example.demo.util.BookState;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.sql.Timestamp;
import lombok.Data;

/**
 * @author 各書籍を管理する。
 */
@Data
@Entity
@Table(name = "BOOKS")
public class Book {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private int id;

  // カスケードは子(このテーブルのデータ)が削除されれば親(参照先)も削除される設定
  @NotNull
  @OneToOne
  @JoinColumn(name = "book_name_id")
  private BookName bookNameId;

  @NotNull @Column(name = "active") private boolean active;

  @NotNull @Column(name = "lendable") private boolean lendable;

  @NotNull @Column(name = "state") private BookState state = BookState.FREE;

  @NotNull @Column(name = "created_at") private Timestamp created_at;

  @NotNull @Column(name = "updated_at") private Timestamp updated_at;
}
