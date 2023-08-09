package com.example.demo.controller.book;

import com.example.demo.model.FormEntity;
import com.example.demo.repository.LendingRepository;
import com.example.demo.repository.UserMngRepository;
import com.example.demo.service.LendingService;
import com.example.demo.service.UserLendingService;
import com.example.demo.util.LendingState;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 返却処理を実装する
 */
@RequiredArgsConstructor
@Controller
public class BookReturnController {
  @Autowired private LendingRepository lendingRepository;
  @Autowired private UserMngRepository userRepository;
  @Autowired private LendingService lendingService;
  @Autowired private UserLendingService userRegisterService;

  /**
   * 返却する本を登録する。
   * 登録するには対象の本の横にあるチェックボックスをチェックする
   * @param user
   * @param form
   * @param model
   * @return
   */
  @GetMapping("/bookReturnRegister")
  public String getBookReturnRegister(Authentication user,
                                      @RequestParam("lendingIds")
                                      String[] lendingIds, Model model) {
    var lendingList = lendingRepository.findListByUserAndState(
        userRepository.findByEmail(user.getName()).get(), LendingState.RENTAL);
    model.addAttribute("lendingList", lendingList);

    var checks = lendingIds;
    model.addAttribute("checks", checks);

    return "BookRental/bookReturn";
  }

  /**
   * 返却する本を確認する
   * @param user
   * @param form
   * @param model
   * @return
   */
  @PostMapping("/bookReturnConfirm")
  public String postBookReturnConfirm(Authentication user,
                                      @ModelAttribute("form") FormEntity form,
                                      Model model) {
    var returnList = lendingRepository.findAllById(form.getChecks());
    model.addAttribute("lendingList", returnList);
    return "BookRental/bookReturnConfirm";
  }

  /**
   * 返却する処理を実行する
   * @param user
   * @param form
   * @param model
   * @return
   */
  @PostMapping("/bookReturnComplete")
  public String postBookReturnComplete(Authentication user,
                                       @ModelAttribute("form") FormEntity form,
                                       Model model) {
    for (var id : form.getChecks()) {
      System.out.println("form.getId(): " + id);
    }
    var returnList = lendingRepository.findAllById(form.getChecks());

    // 返却リストの更新
    lendingService.setLendingReturn(returnList);

    // ユーザーエンティティの貸し出し状態を更新
    var userEntity = userRepository.findByEmail(user.getName()).get();
    userEntity =
        userRegisterService.userSetRentalLending(userEntity, returnList);

    model.addAttribute("lendingList", returnList);
    return "BookRental/bookReturnComplete";
  }
}
