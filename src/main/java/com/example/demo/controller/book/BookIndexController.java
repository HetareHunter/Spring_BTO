package com.example.demo.controller.book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.repository.BookNameRepository;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.GenreRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class BookIndexController
{
	@Autowired
	private BookRepository bookRepository;
	@Autowired
	private BookNameRepository bookNameRepository;
	@Autowired
	private GenreRepository genreRepository;
	
	//private ErrorUtil errorUtil = new ErrorUtil();

	@GetMapping("/bookIndex")
	public String getBookIndex(Authentication user, Model model)
	{
		model.addAttribute("username", user.getName() + "でログインしています。");
		model.addAttribute("bookList", bookRepository.findAll());
		model.addAttribute("bookNameList", bookNameRepository.findAll());
		model.addAttribute("genreList", genreRepository.findAll());
		
		return "BookRental/bookIndex";
	}
}
