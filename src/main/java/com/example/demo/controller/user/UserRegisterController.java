package com.example.demo.controller.user;

import com.example.demo.model.User;
import com.example.demo.repository.UserMngRepository;
import com.example.demo.service.ErrorUtil;
import com.example.demo.util.Authority;
import java.sql.Timestamp;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * ユーザー登録処理
 */
@RequiredArgsConstructor
@Controller
public class UserRegisterController {
  @Autowired private UserMngRepository userRepository;

  private final PasswordEncoder passwordEncoder;

  private ErrorUtil errorUtil = new ErrorUtil();

  /**
   * ユーザーの新規登録
   * @param user
   * @param model
   * @return
   */
  @GetMapping("/register")
  public String register(@ModelAttribute User user, Model model) {
    model.addAttribute("headline", "新規登録");
    return "register";
  }

  /**
   * ユーザー情報の変更
   * @param id
   * @param model
   * @return
   */
  @GetMapping("/register/{id}")
  public String editUser(@PathVariable int id, Model model) {
    model.addAttribute("user", userRepository.findById(id));
    model.addAttribute("headline", "ユーザー情報の編集");
    return "Auth/Alter/alterUserInfo";
  }

  /**
   * ユーザー情報の削除
   * @param id
   * @param loginUser
   * @param model
   * @return
   */
  @GetMapping("/delete/{id}")
  public String deleteUser(@PathVariable int id, Authentication loginUser,
                           Model model) {
    userRepository.deleteById(id);
    model.addAttribute("username", loginUser.getName());
    return "redirect:/index";
  }

  /**
   * ユーザー情報登録内容の確認
   * @param user
   * @param result
   * @param model
   * @return
   *     登録内容に問題がある場合登録画面に戻る。問題なければ確認画面に遷移する
   */
  @PostMapping("/confirm")
  public String confirm(@Validated @ModelAttribute User user,
                        BindingResult result, Model model) {
    // 入力内容に問題がある場合、registerに戻る
    if (result.hasErrors()) {
      errorUtil.printErrorLog(result);
      model.addAttribute("headline", "新規登録");
      return "register";
    }

    // パスワードは暗号化する
    user.setPassword(passwordEncoder.encode(user.getPassword()));

    // 管理者権限はフォームから設定できないようにしている
    user.setRole(Authority.USER);
    // if (user.isAdmin()) {
    //   user.setRole(Authority.ADMIN);
    // } else {
    //   user.setRole(Authority.USER);
    // }

    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    user.setCreated_at(timestamp);
    user.setUpdated_at(timestamp);

    model.addAttribute("headline", "変更内容の確認");
    return "confirm";
  }

  /**
   * 既に登録されているユーザー情報の編集内容の確認ページの処理
   * @param user
   * @param result
   * @param model
   * @return
   */
  @PostMapping("/alterConfirm")
  public String alterConfirm(@Validated @ModelAttribute User user,
                             BindingResult result, Model model) {

    // 入力内容に問題がある場合、alterUserInfoに戻る
    if (result.hasErrors() && !errorUtil.isOnlyEmailError(result)) {
      model.addAttribute("headline", "ユーザー情報の編集");
      return "Auth/Alter/alterUserInfo";
    }

    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    user.setUpdated_at(timestamp);

    model.addAttribute("headline", "変更内容の確認");
    return "Auth/Alter/alterConfirm";
  }

  /**
   * 新規登録内容を登録する処理
   * @param user
   * @return
   */
  @PostMapping("/complete")
  public String complete(@ModelAttribute User user) {
    user.setName(user.getFirst_name() + " " + user.getLast_name());
    System.out.println(user.getName() + " を登録する");
    userRepository.save(user);
    System.out.println("データに登録された");
    return "redirect:/login";
  }

  /**
   * 既に登録されているユーザー情報の編集内容を登録する処理
   * @param user
   * @param loginUser
   * @param model
   * @return
   */
  @PostMapping("/alterComplete")
  public String alterComplete(@ModelAttribute User user,
                              Authentication loginUser, Model model) {
    user.setName(user.getFirst_name() + " " + user.getLast_name());
    System.out.println(user.getName() + " を登録する");
    userRepository.save(user);
    System.out.println("データに登録された");
    model.addAttribute("username", loginUser.getName());
    model.addAttribute("userList", userRepository.findAll());

    return "redirect:/index";
  }
}
