package com.example.demo.model;

import java.sql.Timestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author umaib 各書籍を管理する。
 */
@Data
@Entity
@SequenceGenerator(name = "BOOK_GENERATOR", sequenceName = "bookSeq", allocationSize = 1)
@Table(name = "BOOKS")
public class Book
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "BOOK_GENERATOR")
	@Column(name = "id")
	private int id;

	//カスケードは子(このテーブルのデータ)が削除されれば親(参照先)も削除される設定
	@NotBlank
	@Column(name = "book_name_id")
	@OneToOne(mappedBy="BOOK_NAMES", cascade = CascadeType.ALL)
	@JoinColumn(name = "id")
	private int bookNameId;

	@Column(name = "avtive")
	private boolean avtive;

	@Column(name = "lendable")
	private boolean lendable;

	@Column(name = "created_at")
	private Timestamp created_at;

	@Column(name = "updated_at")
	private Timestamp updated_at;
}
