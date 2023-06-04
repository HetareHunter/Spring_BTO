package com.example.demo.service;

import com.example.demo.model.WeatherEntity;
import com.example.demo.repository.LendingRepository;
import com.example.demo.repository.UserMngRepository;
import com.example.demo.repository.WeatherRepository;
import com.example.demo.util.LendingState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    //ログインしているかどうかの判定、セット
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

    //ユーザーの貸し出し情報のセット
    var cartLendingList = lendingRepository.findListByUserAndState(
        userRepository.findByEmail(user.getName()).get(), LendingState.CART);
    model.addAttribute("cartLendingList", cartLendingList);
    var rentalList = lendingRepository.findListByUserAndState(
        userRepository.findByEmail(user.getName()).get(), LendingState.RENTAL);
    model.addAttribute("rentalList", rentalList);
    WeatherEntity weatherEntity = weatherRepository.findById(1).get();
    model.addAttribute("weather", weatherEntity);
  }
}
