package com.example.demo.model;

import java.sql.Date;
import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author umaib
 *書籍の貸し出し状況を管理する。
 */
@Data
@Entity
@SequenceGenerator(name = "LENDING_GENERATOR", sequenceName = "lendingSeq", allocationSize = 1)
@Table(name = "LENDINGS")
public class Lending
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "LENDING_GENERATOR")
	@Column(name = "id")
	private int id;

	@NotBlank
	@Column(name = "book_id")
	@ManyToOne
	private int bookId;
	
	@NotBlank
	@Column(name = "user_id")
	@ManyToOne
	private int userId;
	
	//貸し出し日
	@Column(name = "lend_date")
	private Date lendDate;
	
	//返却予定日
	@Column(name = "return_due_date")
	private Date returnDueDate;
	
	//返却日
	@Column(name = "return_date")
	private Date returnDate;
	
	//延滞日数
	@Column(name = "overdue_date")
	private int overdueDate;
	
	@Column(name = "created_at")
	private Timestamp created_at;

	@Column(name = "updated_at")
	private Timestamp updated_at;
}
