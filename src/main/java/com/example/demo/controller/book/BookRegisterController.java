package com.example.demo.controller.book;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.model.BookName;
import com.example.demo.repository.BookNameRepository;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.GenreRepository;
import com.example.demo.service.BookRegisterService;
import com.example.demo.service.ErrorUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class BookRegisterController
{
	@Autowired
	private BookRepository bookRepository;
	@Autowired
	private BookNameRepository bookNameRepository;
	@Autowired
	private GenreRepository genreRepository;

	@Autowired
	private BookRegisterService bookRegisterService;

	private ErrorUtil errorUtil = new ErrorUtil();

	@GetMapping("/bookRegister")
	public String getBookRegister(@ModelAttribute BookName bookName, Model model)
	{
		model.addAttribute("genreList", genreRepository.findAll());
		// プルダウンの初期値を設定する場合は指定
        model.addAttribute("selectedValue", "1");
		return "BookRental/Admin/bookRegister";
	}

	@PostMapping("/bookRegConfirm")
	public String postBookConfirm(@Validated @ModelAttribute BookName bookName, BindingResult result, Model model)
	{
		if (result.hasErrors())
		{
			errorUtil.printErrorLog(result);
			return "BookRental/Admin/bookRegister";
		}

		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		bookName.setCreated_at(timestamp);
		bookName.setUpdated_at(timestamp);
		return "BookRental/Admin/bookConfirm";
	}

	@PostMapping("/bookRegComplete")
	public String complete(Authentication user, @ModelAttribute BookName bookName, Model model)
	{
		System.out.println(bookName.getTitle() + " を登録する");
		bookNameRepository.save(bookName);
		bookRegisterService.bookSave(bookName, model);

		model.addAttribute("username", user.getName() + "でログインしています。");
		model.addAttribute("bookList", bookRepository.findAll());
		model.addAttribute("bookNameList", bookNameRepository.findAll());
		return "BookRental/bookIndex";
	}
}
