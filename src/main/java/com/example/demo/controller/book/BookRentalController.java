package com.example.demo.controller.book;

import java.util.ArrayList;

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
import com.example.demo.util.BookState;
import com.example.demo.util.LendingState;

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
		var lendingList = lendingRepository
				.findListByUserAndState(userRepository.findByEmail(user.getName()).get(), LendingState.CART);
		model.addAttribute("lendingList", lendingList);
		return "BookRental/bookRentalConfirm";
	}

	@GetMapping("/bookRentalComplete")
	public String getBookRentalComplete(Authentication user, Model model)
	{
		model.addAttribute("username", user.getName() + "でログインしています。");

		var books = new ArrayList<Book>();
		books = bookRepository.findByState(BookState.CART);
		for (Book book : books)
		{
			try
			{
				bookRegisterService.bookRentalSave(book); // bookの貸し出し状態を更新
				var userEntity = userRepository.findByEmail(user.getName()).get();
				var cartLendings = lendingRepository.findListByUserAndState(userEntity, LendingState.CART);
				var lendings = lendingService.lendingsSave(cartLendings); // 借りている状態にする
				userEntity = userRegisterService.userSetRentalLending(userEntity, lendings); // ユーザーエンティティの貸し出し状態を更新

			} catch (Exception e)
			{
				System.out.println(e + " が postBookIndex() で発生");
			}
		}
		var lendingList = lendingRepository
				.findListByUserAndState(userRepository.findByEmail(user.getName()).get(), LendingState.RENTAL);
		model.addAttribute("lendingList", lendingList);

		return "BookRental/bookRentalComplete";
	}
}
