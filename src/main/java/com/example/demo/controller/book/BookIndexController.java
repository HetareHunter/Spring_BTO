package com.example.demo.controller.book;

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
import java.util.ArrayList;
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

  @GetMapping("/bookIndex")
  public String getBookIndex(Authentication user, Model model,
                             @ModelAttribute Book book,
                             @ModelAttribute String bookId) {
    model.addAttribute("username", user.getName() + "でログインしています。");
    model.addAttribute("bookList", bookRepository.findAll());
    model.addAttribute("bookNameList", bookNameRepository.findAll());
    var cartLendingList = lendingRepository.findListByUserAndState(
        userRepository.findByEmail(user.getName()).get(), LendingState.CART);
    model.addAttribute("cartLendingList", cartLendingList);
    model.addAttribute("bookState_CART", BookState.CART);
    return "BookRental/bookIndex";
  }

  /**
   * 本の検索処理
   * @param user
   * @param model
   * @param searchStr
   * @return
   */
  @GetMapping("/bookIndex_setSearch")
  public String getSearchBook(Authentication user, Model model,
                              @RequestParam("searchStr") String searchStr) {
    var books = new ArrayList<Book>();
    System.out.println("本のタイトル：" + searchStr);
    try {
      var bookNames = bookNameRepository.findByTitleLike("%" + searchStr + "%");
      for (var bookName : bookNames) {
        books.add(bookRepository.findByBookNameId(bookName).get());
      }

    } catch (Exception e) {
      System.out.println(e.getCause() +
                         " が BookIndexController.getTempLendingBook() で発生");
    }
    model.addAttribute("username", user.getName() + "でログインしています。");
    model.addAttribute("bookList", books);
    var cartLendingList = lendingRepository.findListByUserAndState(
        userRepository.findByEmail(user.getName()).get(), LendingState.CART);
    model.addAttribute("cartLendingList", cartLendingList);
    model.addAttribute("searchStr", searchStr);
    System.out.println(
        "javaのcontrollerクラス側は /bookIndex_setSearch にて検索完了");
    return "BookRental/BookIndexFragment/bookTable :: tableReload";
  }

  /**
   * 本の貸し出しページ外からの本の検索処理
   * @param user
   * @param model
   * @param searchStr
   * @return
   */
  @GetMapping("/bookIndex_setSearchAnotherPage")
  public String
  getSearchBookAnotherPage(Authentication user, Model model,
                           @RequestParam("searchStr") String searchStr) {
    var books = new ArrayList<Book>();
    System.out.println("本のタイトル：" + searchStr);
    try {
      var bookNames = bookNameRepository.findByTitleLike("%" + searchStr + "%");
      for (var bookName : bookNames) {
        books.add(bookRepository.findByBookNameId(bookName).get());
      }

    } catch (Exception e) {
      System.out.println(e.getCause() +
                         " が BookIndexController.getTempLendingBook() で発生");
    }
    model.addAttribute("username", user.getName() + "でログインしています。");
    model.addAttribute("bookList", books);
    var cartLendingList = lendingRepository.findListByUserAndState(
        userRepository.findByEmail(user.getName()).get(), LendingState.CART);
    model.addAttribute("cartLendingList", cartLendingList);
    model.addAttribute("bookNameList", bookNameRepository.findAll());
    model.addAttribute("bookState_CART", BookState.CART);
    model.addAttribute("searchStr", searchStr);
    System.out.println(
        "javaのcontrollerクラス側は /bookIndex_setSearchAnotherPage にて検索完了");
    return "BookRental/bookIndex";
  }

  /**
   * カートに入れるときの処理
   * @param user
   * @param book
   * @param model
   * @param bookId
   * @return
   */
  @GetMapping("/bookIndex_setLending")
  public String
  getTempLendingBook(Authentication user, @ModelAttribute Book book,
                     Model model, @RequestParam("bookId") String bookId,
                     @RequestParam("searchStr") String searchStr) {
    System.out.println("本のID：" + bookId);
    var books = new ArrayList<Book>();
    try {
      book = bookRepository.findById(Integer.parseInt(bookId)).get();
      var bookNames = bookNameRepository.findByTitleLike("%" + searchStr + "%");
      for (var bookName : bookNames) {
        books.add(bookRepository.findByBookNameId(bookName).get());
      }
      if (!book.isLendable()) {
        System.out.println("既に貸し出しされています");
        return "BookRental/BookIndexFragment/bookTable :: tableReload";
      }
      bookRegisterService.bookCartSave(book); // bookの貸し出し状態を更新
      var userEntity = userRepository.findByEmail(user.getName()).get();
      var lend = lendingService.setLendingCart(
          book, userEntity); // カートに入れる状態にする
      userEntity = userRegisterService.userSetCartLending(
          userEntity, lend); // ユーザーエンティティの貸し出し状態を更新

    } catch (Exception e) {
      System.out.println(e.getCause() +
                         " が BookIndexController.getTempLendingBook() で発生");
    }
    model.addAttribute("bookList", books);
    model.addAttribute("bookNameList", bookNameRepository.findAll());
    var cartLendingList = lendingRepository.findListByUserAndState(
        userRepository.findByEmail(user.getName()).get(), LendingState.CART);
    model.addAttribute("cartLendingList", cartLendingList);
    model.addAttribute("bookState_CART", BookState.CART);
    model.addAttribute("searchStr", searchStr);
    System.out.println(
        "javaのcontrollerクラス側は /bookIndex_setLending にて更新完了");
    return "BookRental/BookIndexFragment/bookTable :: tableReload";
  }

  /**
   * カートから取り出すときの処理
   * @param user
   * @param book
   * @param model
   * @param bookId
   * @return
   */
  @GetMapping("/bookIndex_deleteLending")
  public String
  getDeleteTempLendingBook(Authentication user, @ModelAttribute Book book,
                           Model model, @RequestParam("bookId") String bookId,
                           @RequestParam("searchStr") String searchStr) {
    System.out.println("bookIndex_deleteLending");
    var books = new ArrayList<Book>();
    try {
      book = bookRepository.findById(Integer.parseInt(bookId)).get();
      var bookNames = bookNameRepository.findByTitleLike("%" + searchStr + "%");
      for (var bookName : bookNames) {
        books.add(bookRepository.findByBookNameId(bookName).get());
      }
      var lend =
          lendingRepository.findByBookAndState(book, LendingState.CART).get();
      var userEntity = userRepository.findByEmail(user.getName()).get();

      bookRegisterService.bookLendableChange(
          book, true, BookState.FREE); // bookの貸し出し状態を更新
      userEntity = userRegisterService.userSetCartLending(
          userEntity, lend); // ユーザーエンティティの貸し出し状態を更新
      lendingService.deleteLending(lend.getId()); // 貸し出し情報の削除

    } catch (Exception e) {
      System.out.println(
          e.getCause() +
          " が BookIndexController.getDeleteTempLendingBook() で発生");
    }
    model.addAttribute("bookList", books);
    model.addAttribute("bookNameList", bookNameRepository.findAll());
    var cartLendingList = lendingRepository.findListByUserAndState(
        userRepository.findByEmail(user.getName()).get(), LendingState.CART);
    model.addAttribute("cartLendingList", cartLendingList);
    model.addAttribute("bookState_CART", BookState.CART);
    return "BookRental/BookIndexFragment/bookTable :: tableReload";
  }

  @GetMapping("/bookCartConfirm")
  public String getBookCartConfirm(Authentication user, Model model) {
    model.addAttribute("username", user.getName() + "でログインしています。");
    var lendingList = lendingRepository.findListByUserAndState(
        userRepository.findByEmail(user.getName()).get(), LendingState.CART);
    model.addAttribute("lendingList", lendingList);

    return "BookRental/bookCartConfirm";
  }

  @GetMapping("/bookRentalCheck")
  public String getBookRentalConfirm(Authentication user, Model model) {
    model.addAttribute("username", user.getName() + "でログインしています。");
    var lendingList = lendingRepository.findListByUserAndState(
        userRepository.findByEmail(user.getName()).get(), LendingState.RENTAL);
    model.addAttribute("lendingList", lendingList);

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
    model.addAttribute("bookState_CART", BookState.CART);
    return "BookRental/Admin/bookRentalAdminMain";
  }
}
