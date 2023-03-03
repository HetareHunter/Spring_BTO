package com.example.demo.controller.book;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.model.Book;
import com.example.demo.repository.BookNameRepository;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.UserMngRepository;
import com.example.demo.service.BookRentalService;

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
	private UserMngRepository userRepository;
	@Autowired
	private BookRentalService bookRentalService;

	// private ErrorUtil errorUtil = new ErrorUtil();
	@Autowired
	Map<Integer, Integer> bookSelected = new HashMap<Integer, Integer>();

	@GetMapping("/bookIndex")
	public String getBookIndex(Authentication user, @ModelAttribute Book book, Model model)
	{
		model.addAttribute("username", user.getName() + "でログインしています。");
		model.addAttribute("bookList", bookRepository.findAll());
		model.addAttribute("bookNameList", bookNameRepository.findAll());
		model.addAttribute("selectBook", bookSelected);

		return "BookRental/bookIndex";
	}

	@GetMapping("/bookIndex/{bookId}")
	public String postBookIndex(Authentication user, @ModelAttribute Book book, Model model, @PathVariable int bookId)
	{
		model.addAttribute("username", user.getName() + "でログインしています。");
		model.addAttribute("bookList", bookRepository.findAll());
		model.addAttribute("bookNameList", bookNameRepository.findAll());
		model.addAttribute("selectBook", bookSelected);
		try
		{
			bookSelected = bookRentalService
					.selectedBook(userRepository.findByEmail(user.getName()).get().getId(), bookId);
			System.out.println(bookSelected.keySet() + " " + bookSelected.values());
			for (int id : bookSelected.keySet())
			{
				System.out.println(bookRepository.findById(id).get().getBookNameId().getTitle());
			}
		} catch (NoSuchElementException e)
		{
			System.out.println(e + " 発生");
		}

		return "BookRental/bookIndex";
	}

	@PostMapping("/bookRentalConfirm")
	public String postBookRentalConfirm(Authentication user, @ModelAttribute Book book, Model model)
	{
		model.addAttribute("username", user.getName() + "でログインしています。");
		model.addAttribute("bookNameList", bookNameRepository.findAll());

		return "BookRental/bookRentalConfirm";
	}
}
