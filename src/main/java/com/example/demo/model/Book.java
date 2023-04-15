package com.example.demo.model;

import com.example.demo.util.BookState;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.sql.Timestamp;
import lombok.Data;

/**
 * @author 各書籍を管理する。
 */
@Data
@Entity
// @SequenceGenerator(name = "BOOK_GENERATOR", sequenceName = "bookSeq",
// allocationSize = 1)
@Table(name = "BOOKS")
public class Book {
  @Id
  //   @GeneratedValue(strategy = GenerationType.IDENTITY,
  //                   generator = "BOOK_GENERATOR")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private int id;

  // カスケードは子(このテーブルのデータ)が削除されれば親(参照先)も削除される設定
  @OneToOne @JoinColumn(name = "book_name_id") private BookName bookNameId;

  @Column(name = "active") private boolean active;

  @Column(name = "lendable") private boolean lendable;

  @Column(name = "state") private BookState state = BookState.FREE;

  @Column(name = "created_at") private Timestamp created_at;

  @Column(name = "updated_at") private Timestamp updated_at;
}
