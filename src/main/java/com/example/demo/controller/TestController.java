package com.example.demo.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.model.User;
import com.example.demo.repository.TestRepository;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class TestController
{
	private final TestRepository testRepository;

	@GetMapping("/")
	public String write1(Model model)
	{
		List<User> list = testRepository.getAll();
		model.addAttribute("userList", list);
		return "index";
	}
	
	@GetMapping("/register")
	public String register(@ModelAttribute User user) {
		return "register";
	}
	
	@PostMapping("/confirm")
	public String confirm(@ModelAttribute User user) {
		
		return "confirm";
	}
	
	@GetMapping("/index")
	public String index(Model model) {
		
		return "index";
	}
}
