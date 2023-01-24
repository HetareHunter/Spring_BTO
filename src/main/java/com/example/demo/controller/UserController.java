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
	@Autowired
	private UserMngRepository userMngRepository;

	private String editHeadline = "";
	private User conUser = new User();

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
		editHeadline = "新規登録";
		model.addAttribute("headline", editHeadline);
		conUser.setName(user.getName());
		return "register";
	}

	@GetMapping("/register/{id}")
	public String editUser(@PathVariable Long id, Model model)
	{
		model.addAttribute("user", userMngRepository.findById(id));
		editHeadline = "ユーザー情報編集";
		model.addAttribute("headline", editHeadline);
		return "register";
	}

	@GetMapping("/delete/{id}")
	public String DeleteUser(@PathVariable Long id)
	{
		userMngRepository.deleteById(id);
		return "redirect:/index";
	}

	@PostMapping("/confirm")
	public String confirm(@Validated @ModelAttribute User user, BindingResult result, Model model)
	{
		conUser = user;
		System.out.println(conUser.getName());

		if (result.hasErrors())
		{
			model.addAttribute("headline", editHeadline);
			return "register";
		}
		// userMngRepository.save(user);//現状の問題点。ここで更新するとその回数分同じデータが登録されてしまっている。このページを開いた時点でデータベースに書き込まれてしまっている
		return "confirm";
	}

	@PostMapping("/complete")
	public String complete()
	{
		System.out.println(conUser.getName() + " を登録する");
		userMngRepository.save(conUser);
		System.out.println("データに登録された");
		return "redirect:/index";
	}

	@GetMapping("/index")
	public String getindex(@ModelAttribute User user, Model model)
	{
		model.addAttribute("userList", userMngRepository.findAll());
		return "index";
	}

	@PostMapping("/index")
	public String postindex(@ModelAttribute User user, Model model)
	{
		model.addAttribute("userList", userMngRepository.findAll());
		return "index";
	}
}
