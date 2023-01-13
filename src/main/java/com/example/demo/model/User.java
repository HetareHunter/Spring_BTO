package com.example.demo.model;

import java.math.BigDecimal;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class User
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private BigDecimal  id;
	
	@Size(min = 2, max = 20)
	private String name;
	
	@Size(min = 4, max = 255)
	private String password;
	
	@NotBlank
	@Email
	private String email;
	
	private String state;
}
