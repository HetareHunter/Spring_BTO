package com.example.demo.controller.book;

import com.example.demo.model.FormEntity;
import com.example.demo.model.Lending;
import com.example.demo.repository.BookNameRepository;
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
import java.util.LinkedHashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * ユーザーが借りたい本を検索し、カートに入れるページ移動等の処理を担う
 */
@RequiredArgsConstructor
@Controller
public class BookIndexController {
  @Autowired private BookRepository bookRepository;
  @Autowired private BookNameRepository bookNameRepository;
  @Autowired private UserMngRepository userRepository;
  @Autowired private LendingRepository lendingRepository;
  @Autowired private LendingService lendingService;
  @Autowired private BookRegisterService bookRegisterService;
  @Autowired private UserLendingService userRegisterService;
  @Autowired private BookSearchService bookSearchService;
  @Autowired private TopbarService topbarService;

  /**
   * 本の検索をせずにアクセスするときのメソッド
   * @param user
   * @param model
   * @return
   */
  @GetMapping("/bookIndex")
  public String getBookIndex(Authentication user, Model model,
                             ModelMap modelMap) {
    bookSearchService.setSearchBookModel(model, modelMap, "");
    topbarService.setTopbarModel(user, model);

    return "BookRental/bookIndex";
  }

  /**
   * bookIndex(本の貸し出しページ)から本の検索をしてアクセスするときの処理
   * @param user
   * @param model
   * @param searchStr 検索した文字列
   * @return ページは読み直さずにテンプレートを差し替える
   */
  @GetMapping("/bookIndex_setSearch")
  public String getSearchBook(Authentication user, Model model,
                              ModelMap modelMap,
                              @RequestParam("searchStr") String searchStr) {
    bookSearchService.setSearchBookModel(model, modelMap, searchStr);
    topbarService.setTopbarModel(user, model);

    System.out.println(
        "javaのcontrollerクラス側は /bookIndex_setSearch にて検索完了");
    return "BookRental/BookIndexFragment/bookTable :: tableReload";
  }

  /**
   * bookIndex(本の貸し出しページ)外から本の検索をしてアクセスするときの処理
   * @param user
   * @param model
   * @param searchStr 検索した文字列
   * @return 新しくページを読み直す
   */
  @GetMapping("/bookIndex_setSearchAnotherPage")
  public String
  getSearchBookAnotherPage(Authentication user, Model model, ModelMap modelMap,
                           @RequestParam("searchStr") String searchStr) {
    bookSearchService.setSearchBookModel(model, modelMap, searchStr);
    topbarService.setTopbarModel(user, model);

    System.out.println(
        "javaのcontrollerクラス側は /bookIndex_setSearchAnotherPage にて検索完了");
    return "BookRental/bookIndex";
  }

  /**
   * 選択した本をカートに入れるときの処理。ajaxで呼び出される一部更新処理(本のテーブル)
   * 本を検索した情報はそのままとする
   * @param user
   * @param model
   * @param bookId カートに入れる本のID
   * @param searchStr 検索した文字列
   * @return
   */
  @GetMapping("/bookIndex_setLending")
  public String
  getTempLendingBook(Authentication user, Model model, ModelMap modelmap,
                     @RequestParam("bookId") String bookId,
                     @RequestParam("searchStr") String searchStr) {
    System.out.println("本のID：" + bookId);

    // 検索情報の保持
    var bookSearchName = bookSearchService.findByTitleBookLike(searchStr);

    var cartBookName =
        bookNameRepository.findById(Integer.parseInt(bookId)).get();
    // 借りる対象の本が0でないとき、カートに追加できるようにする。他人と同時にカートを入れても対応できるように
    var cartInBook =
        bookRepository.findTopByBookNameIdAndState(cartBookName, BookState.FREE)
            .get();
    Integer bookIntegerId = cartInBook.getId();

    // 戻り値が空でない場合は後の処理を行わずそのまま本のテーブルを返す
    var returnStr =
        bookSearchService.setLendingCartBook(user, bookIntegerId.toString());
    if (!returnStr.equals("")) {
      System.out.println("returnStr：" + returnStr);
      return returnStr;
    }

    bookSearchService.setLendingModel(user, model, modelmap, bookSearchName,
                                      searchStr);
    System.out.println("bookIndex_setLending の searchStr : " + searchStr);
    System.out.println(
        "javaのcontrollerクラス側は /bookIndex_setLending にて更新完了");
    return "BookRental/BookIndexFragment/bookTable :: tableReload";
  }

  /**
   * 選択した本をカートから取り出すときの処理。ajaxで呼び出される一部更新処理(本のテーブル)
   * 本を検索した情報はそのままとする
   * @param user
   * @param model
   * @param bookId カートに入れる本のID
   * @param searchStr 検索した文字列
   * @return
   */
  @GetMapping("/bookIndex_deleteLending")
  public String
  getDeleteTempLendingBook(Authentication user, Model model, ModelMap modelmap,
                           @RequestParam("bookId") String bookId,
                           @RequestParam("searchStr") String searchStr) {
    System.out.println("bookIndex_deleteLending");
    // 検索情報の保持
    var bookSearchName = bookSearchService.findByTitleBookLike(searchStr);

    var cartDeleteBookName =
        bookNameRepository.findById(Integer.parseInt(bookId)).get();
    // ユーザーの貸し借り状況を照会して本の名前で借りた本を検索している。非効率なので後で修正したい
    var lendingList = new LinkedHashMap<String, Lending>();
    lendingList = bookSearchService.setAllLendingList(lendingList);
    var cartDeleteBook = lendingList.get(cartDeleteBookName.getTitle());
    Integer bookIntegerId = cartDeleteBook.getBook().getId();

    bookSearchService.deleteLendingCartBook(user, bookIntegerId.toString());
    bookSearchService.setLendingModel(user, model, modelmap, bookSearchName,
                                      searchStr);
    return "BookRental/BookIndexFragment/bookTable :: tableReload";
  }

  /**
   * カートの確認ページに遷移
   * @param user
   * @param model
   * @return
   */
  @GetMapping("/bookCartConfirm")
  public String getBookCartConfirm(Authentication user, Model model) {
    var cartLendingList = lendingRepository.findListByUserAndState(
        userRepository.findByEmail(user.getName()).get(), LendingState.CART);
    model.addAttribute("cartLendingList", cartLendingList);
    topbarService.setTopbarModel(user, model);

    return "BookRental/bookCartConfirm";
  }

  /**
   * 借りるか確認するページに遷移
   * @param user
   * @param model
   * @return
   */
  @GetMapping("/bookRentalCheck")
  public String getBookRentalConfirm(Authentication user, Model model,
                                     @ModelAttribute("form") FormEntity form) {
    var rentalList = lendingRepository.findListByUserAndState(
        userRepository.findByEmail(user.getName()).get(), LendingState.RENTAL);
    model.addAttribute("rentalList", rentalList);
    topbarService.setTopbarModel(user, model);
    var checks = form.getChecks();
    model.addAttribute("checks", checks);
    return "BookRental/bookRentalCheck";
  }

  /**
   * 開発環境で開発者が全ての貸し借り関係を削除する処理を行う
   * @param user
   * @param model
   * @return
   */
  @GetMapping("/deleteLending")
  public String getDeleteLending(Authentication user, Model model) {
    lendingService.deleteAllLending();
    bookRegisterService.bookAllLendableChange(true);
    userRegisterService.deleteAllLendingRelationship();
    return "redirect:/bookIndex";
  }

  /**
   * 管理者用ページに遷移
   * @param user
   * @param model
   * @return
   */
  @GetMapping("/bookAdminMain")
  public String getBookAdminMain(Authentication user, Model model) {
    topbarService.setTopbarModel(user, model);

    model.addAttribute("bookList", bookRepository.findAll());
    model.addAttribute("bookNameList", bookNameRepository.findAll());
    model.addAttribute("bookState_CART", BookState.CART);
    return "BookRental/Admin/bookRentalAdminMain";
  }
}