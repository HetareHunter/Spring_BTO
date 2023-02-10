package com.example.demo.model;

import java.sql.Timestamp;

import com.example.demo.util.Authority;
import com.example.demo.validator.UniqueLogin;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@SequenceGenerator(name = "USER_GENERATOR", sequenceName = "testSeq", allocationSize = 1)
@Table(name = "USERS")
public class User
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "USER_GENERATOR")	
	@Column(name = "id")
	private int id;
	
	@NotNull
	@Size(max = 128)
	@Column(name = "last_name")
	private String last_name;
	
	@NotNull
	@Size(max = 128)
	@Column(name = "first_name")
	private String first_name;
	
	@NotNull
	@Email
	@UniqueLogin
	@Column(name = "email")
	private String email;
	
	@NotNull
	@Size(max = 255)
	@Column(name = "password")
	private String password;
	
	@Column(name = "role")
	private Authority role;
	
	@NotNull
	@Column(name = "created_at")
	private Timestamp created_at;
	
	@NotNull
	@Column(name = "updated_at")
	private Timestamp updated_at;
	
	
	private boolean admin;
}
