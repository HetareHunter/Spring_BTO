package com.example.demo.service;

import com.example.demo.model.WeatherEntity;
import com.example.demo.repository.LendingRepository;
import com.example.demo.repository.UserMngRepository;
import com.example.demo.repository.WeatherRepository;
import com.example.demo.util.LendingState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

/**
 * 各ページの一番上にあるメニューの各種処理を担う
 */
@Service
public class TopbarService {
  @Autowired private UserMngRepository userRepository;
  @Autowired private LendingRepository lendingRepository;
  @Autowired private WeatherRepository weatherRepository;

  /**
   * トップバーに共通して必要な情報をセットするメソッド
   * @param user
   * @param model
   */
  public void setTopbarModel(Authentication user, Model model) {

    // ログインしているかどうかの判定、セット
    Authentication loginUser =
        SecurityContextHolder.getContext().getAuthentication();
    var principal = loginUser.getPrincipal();
    if (principal instanceof UserDetails) {
      var userPrincipal = ((UserDetails)principal);
      System.out.println(userPrincipal.getUsername() + "でログインしています");
      var userName = userRepository.findByEmail(userPrincipal.getUsername())
                         .get()
                         .getName();
      model.addAttribute("username", userName + " さん");
      model.addAttribute("isLogin", true);

    } else {
      model.addAttribute("username", "ログインしていません。");
      model.addAttribute("isLogin", false);
      return;
    }

    // ユーザーID情報のセット
    model.addAttribute(
        "userID",
        userRepository.findByEmail(loginUser.getName()).get().getId());

    // ユーザーの貸し出し情報のセット
    var cartLendingList = lendingRepository.findListByUserAndState(
        userRepository.findByEmail(user.getName()).get(), LendingState.CART);
    model.addAttribute("cartLendingList", cartLendingList);
    var rentalList = lendingRepository.findListByUserAndState(
        userRepository.findByEmail(user.getName()).get(), LendingState.RENTAL);
    model.addAttribute("rentalList", rentalList);

    // 天気情報のセット
    WeatherEntity weatherEntity = weatherRepository.findById(1).get();
    model.addAttribute("weather", weatherEntity);
  }
}
