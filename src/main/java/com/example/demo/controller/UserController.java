package com.example.demo.controller;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
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
import com.example.demo.service.ErrorUtil;
import com.example.demo.util.Authority;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class UserController
{
	@Autowired
	private UserMngRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	private String editHeadline = "";
	private ErrorUtil errorUtil = new ErrorUtil();

	@GetMapping("/")
	public String mainSite(Authentication loginUser, Model model)
	{
		// System.out.println("メインサイト");
		model.addAttribute("username", loginUser.getName());
//		if(loginUser.getAuthorities()==Authority.ADMIN) {
//			
//		}
		model.addAttribute("userList", userRepository.findAll());
		return "index";
	}

	@GetMapping("/admin/list")
	public String showAdminList(Model model)
	{
		model.addAttribute("users", userRepository.findAll());
		return "list";
	}

	@GetMapping("/login")
	public String login()
	{
		return "Auth/login";
	}

//	@GetMapping("/")
//	public String getAllUsers(Model model)
//	{
//		model.addAttribute("userList", userRepository.findAll());
//		return "index";
//	}

	// ユーザーの新規登録
	@GetMapping("/register")
	public String register(@ModelAttribute User user, Model model)
	{
		editHeadline = "新規登録";
		model.addAttribute("headline", editHeadline);
		// conUser.setName(user.getName());
		return "register";
	}

	// ユーザー情報の変更
	@GetMapping("/register/{id}")
	public String editUser(@PathVariable int id, Model model)
	{
		model.addAttribute("user", userRepository.findById(id));
		editHeadline = "ユーザー情報編集";
		model.addAttribute("headline", editHeadline);
		return "/Auth/Alter/alterUserInfo";
	}

	@GetMapping("/delete/{id}")
	public String deleteUser(@PathVariable int id)
	{
		userRepository.deleteById(id);
		return "redirect:/index";
	}

	@PostMapping("/confirm")
	public String confirm(@Validated @ModelAttribute User user, BindingResult result, Model model)
	{
		if (result.hasErrors())
		{
			errorUtil.printErrorLog(result);
			model.addAttribute("headline", editHeadline);
			return "register";
		}

		user.setPassword(passwordEncoder.encode(user.getPassword()));
		if (user.isAdmin())
		{
			user.setRole(Authority.ADMIN);
		} else
		{
			user.setRole(Authority.USER);
		}
		
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		user.setCreated_at(timestamp);
		user.setUpdated_at(timestamp);
		return "confirm";
	}

	@PostMapping("/alterConfirm")
	public String alterConfirm(@Validated @ModelAttribute User user, BindingResult result, Model model)
	{

		if (result.hasErrors() && !errorUtil.isOnlyUserIDError(result))
		{
			model.addAttribute("headline", editHeadline);
			return "/Auth/Alter/alterUserInfo";
		}
		return "/Auth/Alter/alterConfirm";
	}

	@PostMapping("/complete")
	public String complete(@ModelAttribute User user)
	{
		System.out.println(user.getFirst_name() + " " + user.getLast_name() + " " + " を登録する");
		userRepository.save(user);
		System.out.println("データに登録された");
		return "redirect:Auth/login";
	}

	@PostMapping("/alterComplete")
	public String alterComplete(@ModelAttribute User user, Authentication loginUser, Model model)
	{
		System.out.println(user.getFirst_name() + " " + user.getLast_name() + " " + " を登録する");
		userRepository.save(user);
		System.out.println("データに登録された");
		model.addAttribute("username", loginUser.getName());
		model.addAttribute("userList", userRepository.findAll());
		return "index";
	}

	@GetMapping("/index")
	public String getindex(Model model)
	{
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
