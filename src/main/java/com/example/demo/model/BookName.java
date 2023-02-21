package com.example.demo.model;

import java.sql.Timestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * @author umaib
 *書籍名や詳細を管理する。
 */
@Data
@Entity
@SequenceGenerator(name = "BOOK_NAME_GENERATOR", sequenceName = "bookNameSeq", allocationSize = 1)
@Table(name = "BOOK_NAMES")
public class BookName
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "BOOK_NAME_GENERATOR")
	@Column(name = "id")
	private int id;
	
	@NotBlank
	@Size(max = 128)
	@Column(name = "title")
	private String title;
	
	@NotBlank
	@Size(max = 128)
	@Column(name = "author")
	private String author;
	
	@NotBlank
	@Size(max = 255)
	@Column(name = "detail")
	private String detail;
	
	@NotBlank
	@Size(max = 128)
	@Column(name = "publisher")
	private String publisher;
	
	@NotBlank
	@Column(name = "genre")
	private int genre;
	
	@NotBlank
	@Size(max = 255)
	@Column(name = "img")
	private String img;
	
	@Column(name = "avtive")
	private boolean avtive;
	
	// カスケードは子(このテーブルのデータ)が削除されれば親(参照先)も削除される設定
	@OneToOne(mappedBy = "bookName", cascade = CascadeType.ALL)
	private Book book;
	
	@Column(name = "created_at")
	private Timestamp created_at;

	@Column(name = "updated_at")
	private Timestamp updated_at;
}
