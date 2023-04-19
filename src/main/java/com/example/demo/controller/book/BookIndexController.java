package com.example.demo.controller.book;

import com.example.demo.model.Book;
import com.example.demo.repository.BookNameRepository;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.LendingRepository;
import com.example.demo.repository.UserMngRepository;
import com.example.demo.service.BookRegisterService;
import com.example.demo.service.BookSearchService;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@Controller
public class BookIndexController {
  @Autowired private BookRepository bookRepository;
  @Autowired private BookNameRepository bookNameRepository;
  @Autowired private UserMngRepository userRepository;
  @Autowired private LendingRepository lendingRepository;
  @Autowired private LendingService lendingService;
  @Autowired private BookRegisterService bookRegisterService;
  @Autowired private UserRegisterService userRegisterService;
  @Autowired private BookSearchService bookSearchService;

  @GetMapping("/bookIndex")
  public String getBookIndex(Authentication user, Model model,
                             @ModelAttribute Book book,
                             @ModelAttribute String bookId) {
    bookSearchService.setSearchBookModel(user, model, "");
    return "BookRental/bookIndex";
  }

  /**
   * 本の検索処理
   * @param user
   * @param model
   * @param searchStr
   * @return ページは読み直さずにテンプレートを差し替える
   */
  @GetMapping("/bookIndex_setSearch")
  public String getSearchBook(Authentication user, Model model,
                              @RequestParam("searchStr") String searchStr) {
    bookSearchService.setSearchBookModel(user, model, searchStr);
    System.out.println(
        "javaのcontrollerクラス側は /bookIndex_setSearch にて検索完了");
    return "BookRental/BookIndexFragment/bookTable :: tableReload";
  }

  /**
   * 本の貸し出しページ外からの本の検索処理
   * @param user
   * @param model
   * @param searchStr
   * @return 新しくページを読み直す
   */
  @GetMapping("/bookIndex_setSearchAnotherPage")
  public String
  getSearchBookAnotherPage(Authentication user, Model model,
                           @RequestParam("searchStr") String searchStr) {
    bookSearchService.setSearchBookModel(user, model, searchStr);
    System.out.println(
        "javaのcontrollerクラス側は /bookIndex_setSearchAnotherPage にて検索完了");
    return "BookRental/bookIndex";
  }

  /**
   * 選択した本をカートに入れるときの処理
   * @param user
   * @param model
   * @param bookId
   * @return
   */
  @GetMapping("/bookIndex_setLending")
  public String
  getTempLendingBook(Authentication user, Model model,
                     @RequestParam("bookId") String bookId,
                     @RequestParam("searchStr") String searchStr) {
    System.out.println("本のID：" + bookId);
    var books = bookSearchService.findByTitleBookLike(searchStr);
    var returnStr = bookSearchService.setLendingCartBook(user, bookId);
    if (!returnStr.equals("")) {
      System.out.println("returnStr：" + returnStr);
      return "BookRental/BookIndexFragment/bookTable :: tableReload";
    }
    bookSearchService.setLendingModel(user, model, books, searchStr);
    System.out.println("bookIndex_setLending の searchStr : " + searchStr);
    System.out.println(
        "javaのcontrollerクラス側は /bookIndex_setLending にて更新完了");
    return "BookRental/BookIndexFragment/bookTable :: tableReload";
  }

  /**
   * カートから取り出すときの処理
   * @param user
   * @param model
   * @param bookId
   * @return
   */
  @GetMapping("/bookIndex_deleteLending")
  public String
  getDeleteTempLendingBook(Authentication user, Model model,
                           @RequestParam("bookId") String bookId,
                           @RequestParam("searchStr") String searchStr) {
    System.out.println("bookIndex_deleteLending");
    var books = bookSearchService.findByTitleBookLike(searchStr);
    var returnStr = bookSearchService.deleteLendingCartBook(user, bookId);
    if (!returnStr.equals("")) {
      System.out.println("returnStr：" + returnStr);
      return "BookRental/BookIndexFragment/bookTable :: tableReload";
    }
    bookSearchService.setLendingModel(user, model, books, searchStr);
    return "BookRental/BookIndexFragment/bookTable :: tableReload";
  }

  @GetMapping("/bookCartConfirm")
  public String getBookCartConfirm(Authentication user, Model model) {
    model.addAttribute("username", user.getName() + "でログインしています。");
    var cartLendingList = lendingRepository.findListByUserAndState(
        userRepository.findByEmail(user.getName()).get(), LendingState.CART);
    model.addAttribute("cartLendingList", cartLendingList);

    return "BookRental/bookCartConfirm";
  }

  @GetMapping("/bookRentalCheck")
  public String getBookRentalConfirm(Authentication user, Model model) {
    model.addAttribute("username", user.getName() + "でログインしています。");
    var rentalList = lendingRepository.findListByUserAndState(
        userRepository.findByEmail(user.getName()).get(), LendingState.RENTAL);
    model.addAttribute("rentalList", rentalList);

    return "BookRental/bookRentalCheck";
  }

  @GetMapping("/deleteLending")
  public String getDeleteLending(Authentication user, Model model) {
    lendingService.deleteAllLending();
    bookRegisterService.bookAllLendableChange(true);
    userRegisterService.deleteAllLendingRelationship();
    return "redirect:/bookIndex";
  }

  @GetMapping("/bookAdminMain")
  public String getbookAdminMain(Authentication user, Model model) {
    model.addAttribute("username", user.getName() + "でログインしています。");
    model.addAttribute("bookList", bookRepository.findAll());
    model.addAttribute("bookNameList", bookNameRepository.findAll());
    var cartLendingList = lendingRepository.findListByUserAndState(
        userRepository.findByEmail(user.getName()).get(), LendingState.CART);
    model.addAttribute("cartLendingList", cartLendingList);
    var rentalList = lendingRepository.findListByUserAndState(
        userRepository.findByEmail(user.getName()).get(), LendingState.RENTAL);
    model.addAttribute("rentalList", rentalList);
    model.addAttribute("bookState_CART", BookState.CART);
    return "BookRental/Admin/bookRentalAdminMain";
  }
}
