package com.example.demo.controller.book;

import com.example.demo.model.Book;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.LendingRepository;
import com.example.demo.repository.UserMngRepository;
import com.example.demo.service.BookRegisterService;
import com.example.demo.service.BookSearchService;
import com.example.demo.service.LendingService;
import com.example.demo.service.TopbarService;
import com.example.demo.service.UserLendingService;
import com.example.demo.util.BookState;
import com.example.demo.util.LendingState;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 本を借りるときの処理を実装する
 */
@RequiredArgsConstructor
@Controller
public class BookRentalController {
  @Autowired private BookRepository bookRepository;
  @Autowired private UserMngRepository userRepository;
  @Autowired private LendingRepository lendingRepository;
  @Autowired private LendingService lendingService;
  @Autowired private TopbarService topbarService;
  @Autowired private BookRegisterService bookRegisterService;
  @Autowired private UserLendingService userRegisterService;
  @Autowired private BookSearchService bookSearchService;

  /**
   * 借りる内容が正しいか確認する
   * @param user
   * @param book
   * @param model
   * @return
   */
  @GetMapping("/bookRental")
  public String getBookRentalConfirm(Authentication user,
                                     @ModelAttribute Book book, Model model) {
    var lendingList = lendingRepository.findListByUserAndState(
        userRepository.findByEmail(user.getName()).get(), LendingState.CART);
    model.addAttribute("lendingList", lendingList);
    return "BookRental/bookRentalConfirm";
  }

  /**
   * 借りる処理を実行する
   * @param user
   * @param model
   * @return
   */
  @GetMapping("/bookRentalComplete")
  public String getBookRentalComplete(Authentication user, Model model) {
    topbarService.setTopbarModel(user, model);
    var books = new ArrayList<Book>();
    books = bookRepository.findByState(BookState.CART);
    for (Book book : books) {
      try {
        // bookの貸し出し状態を更新
        bookRegisterService.bookRentalSave(book);
        var userEntity = userRepository.findByEmail(user.getName()).get();
        var cartLendings = lendingRepository.findListByUserAndState(
            userEntity, LendingState.CART);
        // 借りている状態にする
        var lendings = lendingService.setLendingRental(cartLendings);
        // ユーザーエンティティの貸し出し状態を更新
        userEntity =
            userRegisterService.userSetRentalLending(userEntity, lendings);

      } catch (Exception e) {
        System.out.println(e + " が postBookIndex() で発生");
      }
    }
    return "BookRental/bookRentalComplete";
  }

  /**
   * 借りる処理を実行する
   * @param user
   * @param model
   * @return
   */
  @GetMapping("/bookRentalCompleteAjax")
  public String getBookRentalCompleteAjax(Authentication user, Model model) {
    var books = new ArrayList<Book>();
    books = bookRepository.findByState(BookState.CART);
    for (Book book : books) {
      try {
        // bookの貸し出し状態を更新
        bookRegisterService.bookRentalSave(book);
        var userEntity = userRepository.findByEmail(user.getName()).get();
        var cartLendings = lendingRepository.findListByUserAndState(
            userEntity, LendingState.CART);
        // 借りている状態にする
        var lendings = lendingService.setLendingRental(cartLendings);
        // ユーザーエンティティの貸し出し状態を更新
        userEntity =
            userRegisterService.userSetRentalLending(userEntity, lendings);
      } catch (Exception e) {
        System.out.println(e + " が postBookIndex() で発生");
      }
      topbarService.setTopbarModel(user, model);
    }
    return "BookRental/bookCartCheckFragment/bookCartCheckTable :: cartTableReload";
  }

  /**
   * 選択した本をカートにいるページからカートから取り出すときの処理。ajaxで呼び出される一部更新処理(カートに入っている本のテーブル)
   * @param user
   * @param model
   * @param bookId カートに入れる本のID
   * @param searchStr 検索した文字列
   * @return
   */
  @GetMapping("/bookCartCheck_deleteLending")
  public String
  getDeleteTempLendingBook(Authentication user, Model model,
                           @RequestParam("bookId") String bookId) {
    System.out.println("bookIndex_deleteLending");
    //本を取り出しているのでlending modelを更新
    bookSearchService.deleteLendingCartBook(user, bookId);
    //本を取り出した後のデータでuser modelを更新
    bookSearchService.setCartLendingModel(user, model);
    return "BookRental/bookCartCheckFragment/bookCartCheckTable :: cartTableReload";
  }
}
