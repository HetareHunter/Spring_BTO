package com.example.demo.service;

import com.example.demo.model.Book;
import com.example.demo.repository.BookNameRepository;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.LendingRepository;
import com.example.demo.repository.UserMngRepository;
import com.example.demo.util.BookState;
import com.example.demo.util.LendingState;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

/**
 * 本の検索機能に関する処理を定義する
 */
@RequiredArgsConstructor
@Service
public class BookSearchService {
  @Autowired private BookRepository bookRepository;
  @Autowired private BookNameRepository bookNameRepository;
  @Autowired private UserMngRepository userRepository;
  @Autowired private LendingRepository lendingRepository;
  @Autowired private LendingService lendingService;
  @Autowired private BookRegisterService bookRegisterService;
  @Autowired private UserLendingService userRegisterService;

  /**
   * controller側で同じ処理をして戻り値のみ違うメソッドがあるので
   * BookSearchService で定義する
   * @param user
   * @param model
   * @param searchStr
   */
  public void setSearchBookModel(Model model, String searchStr) {
    var books = findByTitleBookLike(searchStr);

    model.addAttribute("bookList", books);
    model.addAttribute("searchStr", searchStr);
  }

  /**
   * 部分一致で検索する関数
   * @param searchStr
   * @return ヒットした本のリストを返す
   */
  public ArrayList<Book> findByTitleBookLike(String searchStr) {
    var books = new ArrayList<Book>();
    System.out.println("本のタイトル：" + searchStr);
    try {
      var bookNames =
          bookNameRepository.findByTitleLikeOrderByTitle("%" + searchStr + "%");
      for (var bookName : bookNames) {
        books.add(bookRepository.findByBookNameId(bookName).get());
      }

    } catch (Exception e) {
      System.out.println(e.getCause() + " が findByTitleBookLike() で発生");
    }

    return books;
  }

  /**
   * カートに入れるとき、取り出すとき同じモデルを定義しているのでまとめる
   * @param user
   * @param model
   * @param books
   * @param searchStr
   */
  public void setLendingModel(Authentication user, Model model,
                              ArrayList<Book> books, String searchStr) {
    model.addAttribute("bookList", books);
    model.addAttribute("bookNameList", bookNameRepository.findAll());
    var cartLendingList = lendingRepository.findListByUserAndState(
        userRepository.findByEmail(user.getName()).get(), LendingState.CART);
    model.addAttribute("cartLendingList", cartLendingList);
    var rentalList = lendingRepository.findListByUserAndState(
        userRepository.findByEmail(user.getName()).get(), LendingState.RENTAL);
    model.addAttribute("rentalList", rentalList);
    model.addAttribute("bookState_CART", BookState.CART);
    model.addAttribute("searchStr", searchStr);
  }

  /**
   * 本をカートに入れる状態にする、
   * lendingエンティティをカートに入れる状態、
   * ユーザーの本の貸し出し状態にlendingエンティティを追加する処理を実施
   * @param user
   * @param bookId
   * @return
   */
  public String setLendingCartBook(Authentication user, String bookId) {
    try {
      var book = bookRepository.findById(Integer.parseInt(bookId)).get();

      //既に貸し出しされている場合は借りれないようにする(暫定)
      if (!book.isLendable()) {
        System.out.println("既に貸し出しされています");
        return "BookRental/BookIndexFragment/bookTable :: tableReload";
      }

      // bookの貸し出し状態を更新する
      bookRegisterService.bookCartSave(book);
      var userEntity = userRepository.findByEmail(user.getName()).get();

      // 貸し借り状態をカートに入れている状態にする
      var lend = lendingService.setLendingCart(book, userEntity);

      // ユーザーエンティティの貸し出し状態に反映する
      userEntity = userRegisterService.userSetCartLending(userEntity, lend);

    } catch (Exception e) {
      System.out.println(e.getCause() +
                         " が BookIndexController.getTempLendingBook() で発生");
    }

    return "";
  }

  /**
   * 本を借りれる状態、
   * lendingエンティティをカートに入れる状態、
   * ユーザーの本の貸し出し状態にlendingエンティティを追加する処理を実施
   * lendingエンティティを削除する
   * @param user
   * @param bookId
   */
  public void deleteLendingCartBook(Authentication user, String bookId) {
    try {

      var book = bookRepository.findById(Integer.parseInt(bookId)).get();

      var lend =
          lendingRepository.findByBookAndState(book, LendingState.CART).get();
      var userEntity = userRepository.findByEmail(user.getName()).get();

      bookRegisterService.bookLendableChange(
          book, true, BookState.FREE); // bookの貸し出し状態を更新

      // ユーザーエンティティの貸し出し状態を更新
      userEntity = userRegisterService.userDeleteCartLending(userEntity, lend);

      lendingService.deleteLending(lend.getId()); // 貸し出し情報の削除

    } catch (Exception e) {
      System.out.println(
          e.getCause() +
          " が BookSearchService.deleteLendingCartBook() で発生");
    }
  }
}
