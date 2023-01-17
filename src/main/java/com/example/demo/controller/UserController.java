package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.model.User;
import com.example.demo.repository.UserMngRepository;

@Controller
//@RequiredArgsConstructor
public class UserController
{
	// private final UserRepository userRepository;
	@Autowired
	private UserMngRepository userMngRepository;

//	@GetMapping("/")
//	public String write1(Model model)
//	{
//		List<User> list = userRepository.getAll();
//		model.addAttribute("userList", list);
//		return "index";
//	}

	public UserController(UserMngRepository repository)
	{
		this.userMngRepository = repository;
	}

	@GetMapping("/")
	public String getAllUsers(Model model)
	{
		model.addAttribute("userList", userMngRepository.findAll());
		return "index";
	}

	@GetMapping("/register")
	public String register(@ModelAttribute User user, Model model)
	{
		// model.addAttribute("id", userMngRepository.findById(id));
		return "register";
	}

	@GetMapping("/register/{id}")
	public String editUser(@PathVariable Long id, Model model)
	{
		model.addAttribute("user", userMngRepository.findById(id));
		return "register";
	}

	@GetMapping("/delete/{id}")
	public String DeleteUser(@PathVariable Long id)
	{
		userMngRepository.deleteById(id);
		return "redirect:/";
	}

	@PostMapping("/confirm")
	public String confirm(@Validated @ModelAttribute User user, Model model, BindingResult result)
	{
		if (result.hasErrors())
		{
			return "register";
		}
		userMngRepository.save(user);
		return "confirm";
	}

	@PostMapping("/index")
	public String index(Model model)
	{
		model.addAttribute("userList", userMngRepository.findAll());
		return "index";
	}
}
