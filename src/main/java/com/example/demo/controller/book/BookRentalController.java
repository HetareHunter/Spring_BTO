package com.example.demo.controller.book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.example.demo.model.Book;
import com.example.demo.repository.BookNameRepository;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.LendingRepository;
import com.example.demo.repository.UserMngRepository;
import com.example.demo.service.BookRegisterService;
import com.example.demo.service.LendingService;
import com.example.demo.service.UserRegisterService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class BookRentalController
{
	@Autowired
	private BookRepository bookRepository;
	@Autowired
	private BookNameRepository bookNameRepository;
	@Autowired
	private UserMngRepository userRepository;
	@Autowired
	private LendingRepository lendingRepository;
	@Autowired
	private LendingService lendingService;
	@Autowired
	BookRegisterService bookRegisterService;
	@Autowired
	UserRegisterService userRegisterService;
	
	@GetMapping("/bookRental")
	public String getBookRentalConfirm(Authentication user, @ModelAttribute Book book, Model model)
	{
		model.addAttribute("username", user.getName() + "でログインしています。");
		model.addAttribute("lendingList", lendingService
				.searchLending(userRepository.findByEmail(user.getName()).get()));
		return "BookRental/bookRentalConfirm";
	}
	
	@GetMapping("/bookRentalComplete")
	public String getBookRentalComplete(Authentication user, @ModelAttribute Book book, Model model)
	{
		model.addAttribute("username", user.getName() + "でログインしています。");
		model.addAttribute("lendingList", lendingService
				.searchLending(userRepository.findByEmail(user.getName()).get()));
		
//		try
//		{
//			book = bookRepository.findById(bookId).get();
//			if (!book.isLendable())
//			{
//				System.out.println("既に貸し出しされています");
//				return "BookRental/bookIndex";
//			}
//			bookRegisterService.bookLendableChange(book, false); // bookの貸し出し状態を更新
//			var userEntity = userRepository.findByEmail(user.getName()).get();
//			var lend = lendingService.tempLendingSave(book, userEntity); // カートに入れる状態にする
//			userEntity = userRegisterService.changeUserLending(userEntity, lend); // ユーザーエンティティの貸し出し状態を更新
//
//		} catch (Exception e)
//		{
//			System.out.println(e + " が postBookIndex() で発生");
//		}
		return "BookRental/bookRentalComplete";
	}
}
