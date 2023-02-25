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

import com.example.demo.model.Book;
import com.example.demo.model.BookName;
import com.example.demo.repository.BookNameRepository;
import com.example.demo.repository.BookRepository;
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
	
	private ErrorUtil errorUtil = new ErrorUtil();

	@GetMapping("/bookRegister")
	public String getBookRegister(@ModelAttribute BookName bookName, Model model)
	{
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
	public String complete(Authentication user,@ModelAttribute BookName bookName, Model model)
	{
		System.out.println(bookName.getTitle() + " を登録する");
		bookNameRepository.save(bookName);
		var book = new Book();
		book.setActive(true);
		book.setBookNameId(bookNameRepository.findById(bookName.getId()).get());
		if (bookName.isActive())
		{
			book.setLendable(true);
		} else
		{
			book.setLendable(false);
		}
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		book.setCreated_at(timestamp);
		book.setUpdated_at(timestamp);
		bookRepository.save(book);

		System.out.println("データに登録された");
		
		model.addAttribute("username", user.getName() + "でログインしています。");
		model.addAttribute("bookList", bookRepository.findAll());
		model.addAttribute("bookNameList", bookNameRepository.findAll());
		return "BookRental/bookIndex";
	}
}
