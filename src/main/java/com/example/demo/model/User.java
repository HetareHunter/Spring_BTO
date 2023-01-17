package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@SequenceGenerator(name = "USER_GENERATOR", sequenceName = "testSeq", allocationSize = 1)
@Table(name = "test")
public class User
{
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USER_GENERATOR")	
	@Column(name = "ID")
	private Long id;
	
	@Size(min = 2, max = 20)
	@Column(name = "NAME")
	private String name;
	
	@Size(min = 4, max = 255)
	@Column(name = "PASSWORD")
	private String password;
	
	@NotBlank
	@Email
	@Column(name = "EMAIL")
	private String email;
	
	@Column(name = "STATE")
	private String state;
}
