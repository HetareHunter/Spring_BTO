package com.example.demo.controller;

import com.example.demo.repository.LendingRepository;
import com.example.demo.repository.UserMngRepository;
import com.example.demo.util.LendingState;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
@Controller
public class UserController {
  @Autowired private UserMngRepository userRepository;
  @Autowired private LendingRepository lendingRepository;

  // -------------------------メインサイト-------------------------
  @GetMapping("/")
  public String mainSite(Authentication user, Model model) {
    Authentication loginUser =
        SecurityContextHolder.getContext().getAuthentication();

    if (loginUser.getName().equals("anonymousUser")) {
      model.addAttribute("username", "ログインしていません。");
      model.addAttribute("isLogin", false);
    } else if (loginUser.isAuthenticated()) {
      model.addAttribute("username",
                         loginUser.getName() + "でログインしています。");
      model.addAttribute("isLogin", true);
    } else {
      model.addAttribute("username", "ログインしていません。");
      model.addAttribute("isLogin", false);
    }
    var cartLendingList = lendingRepository.findListByUserAndState(
        userRepository.findByEmail(user.getName()).get(), LendingState.CART);
    model.addAttribute("cartLendingList", cartLendingList);
    return "main";
  }

  @GetMapping("/admin/list")
  public String showAdminList(Model model) {
    model.addAttribute("users", userRepository.findAll());
    model.addAttribute("bookCartList",
                       lendingRepository.findListByState(LendingState.CART));
    return "list";
  }

  // -------------------------ログイン画面、ユーザー情報変更画面-------------------------

  @GetMapping("/login")
  public String login() {
    return "Auth/login";
  }

  // -------------------------管理者画面-------------------------
  @GetMapping("/index")
  public String getindex(Authentication user, Model model) {
    model.addAttribute("username", user.getName() + "でログインしています。");
    model.addAttribute("userList", userRepository.findAll());
    var cartLendingList = lendingRepository.findListByUserAndState(
        userRepository.findByEmail(user.getName()).get(), LendingState.CART);
    model.addAttribute("cartLendingList", cartLendingList);
    return "index";
  }

  @PostMapping("/index")
  public String postindex(Authentication user, Model model) {
    model.addAttribute("userList", userRepository.findAll());
    var cartLendingList = lendingRepository.findListByUserAndState(
        userRepository.findByEmail(user.getName()).get(), LendingState.CART);
    model.addAttribute("cartLendingList", cartLendingList);
    return "index";
  }
}
