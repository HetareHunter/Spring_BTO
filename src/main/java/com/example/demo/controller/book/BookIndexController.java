package com.example.demo.controller.book;

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
import com.example.demo.repository.LendingRepository;
import com.example.demo.repository.UserMngRepository;
import com.example.demo.service.BookRegisterService;
import com.example.demo.service.LendingService;
import com.example.demo.service.UserRegisterService;
import com.example.demo.util.BookState;
import com.example.demo.util.LendingState;

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
	private LendingRepository lendingRepository;
	@Autowired
	private LendingService lendingService;
	@Autowired
	BookRegisterService bookRegisterService;
	@Autowired
	UserRegisterService userRegisterService;

	@GetMapping("/bookIndex")
	public String getBookIndex(Authentication user, @ModelAttribute Book book, Model model)
	{
		model.addAttribute("username", user.getName() + "でログインしています。");
		model.addAttribute("bookList", bookRepository.findAll());
		model.addAttribute("bookNameList", bookNameRepository.findAll());
		var lendingList = lendingRepository.findListByUser(userRepository.findByEmail(user.getName()).get());
		model.addAttribute("lendingList", lendingList);
		return "BookRental/bookIndex";
	}

	@GetMapping("/bookIndex_setLending/{bookId}")
	public String getTempLendingBook(Authentication user, @ModelAttribute Book book, Model model,
			@PathVariable int bookId)
	{
		try
		{
			book = bookRepository.findById(bookId).get();
			if (!book.isLendable())
			{
				System.out.println("既に貸し出しされています");
				return "BookRental/bookIndex";
			}
			bookRegisterService.bookCartSave(book); // bookの貸し出し状態を更新
			var userEntity = userRepository.findByEmail(user.getName()).get();
			var lend = lendingService.tempLendingSave(book, userEntity); // カートに入れる状態にする
			userEntity = userRegisterService.userSetCartLending(userEntity, lend); // ユーザーエンティティの貸し出し状態を更新

		} catch (Exception e)
		{
			System.out.println(e + " が postBookIndex() で発生");
		}
		return "redirect:/bookIndex";
	}

	@GetMapping("/bookIndex_deleteLending/{bookId}")
	public String getDeleteTempLendingBook(Authentication user, @ModelAttribute Book book, Model model,
			@PathVariable int bookId)
	{
		try
		{
			book = bookRepository.findById(bookId).get();
			var lend = lendingRepository.findByBook(book).get();
			var userEntity = userRepository.findByEmail(user.getName()).get();

			bookRegisterService.bookLendableChange(book, true, BookState.FREE); // bookの貸し出し状態を更新
			userEntity = userRegisterService.userSetCartLending(userEntity, lend); // ユーザーエンティティの貸し出し状態を更新
			lendingService.deleteLending(lend.getId()); // 貸し出し情報の削除

		} catch (Exception e)
		{
			System.out.println(e + " が postBookIndex() で発生");
		}
		return "redirect:/bookIndex";
	}

	@PostMapping("/bookCartConfirm")
	public String postBookRentalConfirm(Authentication user, @ModelAttribute Book book, Model model)
	{
		model.addAttribute("username", user.getName() + "でログインしています。");
		model.addAttribute("lendingList", lendingService
				.searchLendings(userRepository.findByEmail(user.getName()).get(), LendingState.CART));

		return "BookRental/bookCartConfirm";
	}

	@GetMapping("/deleteLending")
	public String getDeleteLending(Authentication user, @ModelAttribute Book book, Model model)
	{
		lendingService.deleteAllLending();
		bookRegisterService.bookAllLendableChange(true);
		userRegisterService.deleteAllLendingRelationship();
		return "redirect:/bookIndex";
	}
}
