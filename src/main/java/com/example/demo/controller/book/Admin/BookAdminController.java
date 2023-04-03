package com.example.demo.controller.book.Admin;

import com.example.demo.repository.BookRepository;
import com.example.demo.repository.LendingRepository;
import com.example.demo.repository.UserMngRepository;
import com.example.demo.service.BookRegisterService;
import com.example.demo.service.LendingService;
import com.example.demo.service.UserRegisterService;
import com.example.demo.util.BookState;
import com.example.demo.util.LendingState;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RequiredArgsConstructor
@Controller
public class BookAdminController {
  @Autowired private UserMngRepository userRepository;
  @Autowired private BookRepository bookRepository;
  @Autowired private LendingRepository lendingRepository;
  @Autowired private LendingService lendingService;
  @Autowired private BookRegisterService bookRegisterService;
  @Autowired private UserRegisterService userRegisterService;

  @GetMapping("/bookLendingAdmin")
  public String getBookLendingAdmin(Authentication user, Model model) {
    model.addAttribute("username", user.getName() + "でログインしています。");
    model.addAttribute("bookList", bookRepository.findAll());
    model.addAttribute("lendingList", lendingRepository.findAll());
    model.addAttribute("bookState_CART", BookState.CART);
    model.addAttribute("lendingState", LendingState.RETURN);
    var cartLendingList = lendingRepository.findListByUserAndState(
        userRepository.findByEmail(user.getName()).get(), LendingState.CART);
    model.addAttribute("cartLendingList", cartLendingList);
    return "BookRental/Admin/bookLendingAdmin";
  }

  @GetMapping("/LendingClose/{lendId}")
  public String getLendingClose(@PathVariable int lendId) {
    try {
      var lend = lendingRepository.findById(lendId).get();
      var book = lend.getBook();
      bookRegisterService.bookReturnSave(book); // bookの貸し出し状態を更新
      var user = lend.getUser();
      lendingService.setLendingClose(lend); // 貸し出し状態のクローズをして更新
      user = userRegisterService.userSetCartLending(
          user, lend); // ユーザーエンティティの貸し出し状態を更新

    } catch (Exception e) {
      System.out.println(e + " が postBookIndex() で発生");
    }
    return "redirect:/bookLendingAdmin";
  }
}
