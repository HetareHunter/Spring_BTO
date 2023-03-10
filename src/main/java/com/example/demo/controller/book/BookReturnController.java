package com.example.demo.controller.book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.model.FormEntity;
import com.example.demo.repository.BookNameRepository;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.GenreRepository;
import com.example.demo.repository.LendingRepository;
import com.example.demo.repository.UserMngRepository;
import com.example.demo.service.BookRegisterService;
import com.example.demo.service.LendingService;
import com.example.demo.service.UserRegisterService;
import com.example.demo.util.LendingState;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class BookReturnController
{
	@Autowired
	private BookRepository bookRepository;
	@Autowired
	private BookNameRepository bookNameRepository;
	@Autowired
	private GenreRepository genreRepository;
	@Autowired
	private LendingRepository lendingRepository;
	@Autowired
	private UserMngRepository userRepository;
	@Autowired
	private LendingService lendingService;
	@Autowired
	private BookRegisterService bookRegisterService;
	@Autowired
	private UserRegisterService userRegisterService;

	@GetMapping("/bookReturnRegister")
	public String getBookReturnRegister(Authentication user, @ModelAttribute("form") FormEntity form, Model model)
	{
		model.addAttribute("username", user.getName() + "でログインしています。");
		var lendingList = lendingRepository
				.findListByUserAndState(userRepository.findByEmail(user.getName()).get(), LendingState.RENTAL);
		model.addAttribute("lendingList", lendingList);

		var checks = form.getChecks();
		model.addAttribute("checks", checks);

		return "BookRental/bookReturn";
	}

	@PostMapping("/bookReturnConfirm")
	public String postBookReturnConfirm(Authentication user, @ModelAttribute("form") FormEntity form, Model model)
	{
		model.addAttribute("username", user.getName() + "でログインしています。");
		var returnList = lendingRepository.findAllById(form.getChecks());
		model.addAttribute("lendingList", returnList);
		return "BookRental/bookReturnConfirm";
	}
	
	@PostMapping("/bookReturnComplete")
	public String postBookReturnComplete(Authentication user, @ModelAttribute("form") FormEntity form, Model model)
	{
		model.addAttribute("username", user.getName() + "でログインしています。");
		for (var id : form.getChecks())
		{
			System.out.println("form.getId(): " + id);
		}
		var returnList = lendingRepository.findAllById(form.getChecks());
		lendingService.returnLendings(returnList);// 返却リストの更新
		var userEntity = userRepository.findByEmail(user.getName()).get();
		userEntity = userRegisterService.userSetRentalLending(userEntity, returnList); // ユーザーエンティティの貸し出し状態を更新
		model.addAttribute("lendingList", returnList);
		return "BookRental/bookReturnComplete";
	}
}
