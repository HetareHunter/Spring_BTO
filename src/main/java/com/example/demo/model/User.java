package com.example.demo.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.example.demo.util.Authority;
import com.example.demo.validator.UniqueLogin;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * @author umaib 本システムにログインできるユーザ、パスワードを管理する
 */
@Data
@Entity
@SequenceGenerator(name = "USER_GENERATOR", sequenceName = "testSeq", allocationSize = 1)
@Table(name = "USERS")
public class User
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "USER_GENERATOR")
	@Column(name = "id")
	private int id;

	@NotBlank
	@Size(max = 128)
	@Column(name = "first_name")
	private String first_name;

	@NotBlank
	@Size(max = 128)
	@Column(name = "last_name")
	private String last_name;

	@NotBlank
	@Email
	@UniqueLogin
	@Column(name = "email")
	private String email;

	@NotBlank
	@Size(max = 255)
	@Column(name = "password")
	private String password;

	@Column(name = "role")
	private Authority role;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
	@Column(name = "lending")
	private List<Lending> lendings = new ArrayList<Lending>();

	@Column(name = "created_at")
	private Timestamp created_at;

	@Column(name = "updated_at")
	private Timestamp updated_at;

	private String name = first_name + " " + last_name;
	private boolean admin;
}
