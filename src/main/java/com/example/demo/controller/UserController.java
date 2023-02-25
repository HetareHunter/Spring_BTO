package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.repository.UserMngRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class UserController
{
	@Autowired
	private UserMngRepository userRepository;

	// -------------------------メインサイト-------------------------
	@GetMapping("/")
	public String mainSite(Model model)
	{
		Authentication loginUser = SecurityContextHolder.getContext().getAuthentication();

		if (loginUser.getName().equals("anonymousUser"))
		{
			model.addAttribute("username", "ログインしていません。");
			model.addAttribute("isLogin", false);
		} else if (loginUser.isAuthenticated())
		{
			model.addAttribute("username", loginUser.getName() + "でログインしています。");
			model.addAttribute("isLogin", true);
		} else
		{
			model.addAttribute("username", "ログインしていません。");
			model.addAttribute("isLogin", false);
		}
		return "main";
	}

	@GetMapping("/admin/list")
	public String showAdminList(Model model)
	{
		model.addAttribute("users", userRepository.findAll());
		return "list";
	}

	// -------------------------ログイン画面、ユーザー情報変更画面-------------------------

	@GetMapping("/login")
	public String login()
	{
		return "Auth/login";
	}

	// -------------------------管理者画面-------------------------
	@GetMapping("/index")
	public String getindex(Authentication user, Model model)
	{
		model.addAttribute("username", user.getName() + "でログインしています。");
		model.addAttribute("userList", userRepository.findAll());
		return "index";
	}

	@PostMapping("/index")
	public String postindex(Model model)
	{
		model.addAttribute("userList", userRepository.findAll());
		return "index";
	}
}
