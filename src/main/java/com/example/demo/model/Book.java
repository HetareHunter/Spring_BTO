package com.example.demo.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
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

	// カスケードは子(このテーブルのデータ)が削除されれば親(参照先)も削除される設定
	@OneToOne
	@JoinColumn(name = "bookNameId")
	private BookName bookName;

	@Column(name = "active")
	private boolean active;

	@Column(name = "lendable")
	private boolean lendable;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "book")
	@Column(name = "lending")
	private List<Lending> lending = new ArrayList<>();

	@Column(name = "created_at")
	private Timestamp created_at;

	@Column(name = "updated_at")
	private Timestamp updated_at;
}
