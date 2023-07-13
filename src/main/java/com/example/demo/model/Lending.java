package com.example.demo.model;

import com.example.demo.util.LendingState;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.sql.Date;
import java.sql.Timestamp;
import lombok.Data;

/**
 * 書籍の貸し出し状況を管理する。
 */
@Data
@Entity
@Table(name = "LENDINGS")
public class Lending {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private int id;

  @NotNull @ManyToOne private Book book;

  @NotNull @ManyToOne private User user;

  // 貸し出し日
  @NotNull @Column(name = "lend_date") private Date lendDate;

  // 返却予定日
  @NotNull @Column(name = "return_due_date") private Date returnDueDate;

  // 返却日
  @Column(name = "return_date") private Date returnDate;

  // 延滞日数
  @Column(name = "overdue_date") private int overdueDate;

  @NotNull
  @Column(name = "state")
  private LendingState state = LendingState.CART;

  @NotNull @Column(name = "created_at") private Timestamp created_at;

  @NotNull @Column(name = "updated_at") private Timestamp updated_at;
}
