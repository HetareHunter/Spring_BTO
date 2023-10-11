package com.example.demo.service;

import com.example.demo.model.Book;
import com.example.demo.model.BookName;
import com.example.demo.model.Lending;
import com.example.demo.repository.BookNameRepository;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.LendingRepository;
import com.example.demo.repository.UserMngRepository;
import com.example.demo.util.BookState;
import com.example.demo.util.LendingState;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;

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
  public void setSearchBookModel(Model model, ModelMap modelMap,
                                 String searchStr) {
    // var books = findByTitleBookLike(searchStr);
    var bookName = bookNameRepository.findByTitleIgnoreCaseLikeOrderByTitleDesc(
        "%" + searchStr + "%");

    for (var book : bookName) {
      System.out.println("bookListの本：" + book.getTitle());
    }
    if (bookName.size() == 0) {
      System.out.println("bookListの本は1つも入ってない");
    }

    // 各本の名前毎に在庫を数える
    var bookCount = new LinkedHashMap<String, Integer>();
    bookCount = allBookCounter(bookCount);

    // 現在の貸し借り状況をmapに格納する
    var lendingList = new LinkedHashMap<String, Lending>();
    lendingList = setAllLendingList(lendingList);

    model.addAttribute("bookList", bookName);
    modelMap.addAttribute("bookCountMap", bookCount);
    modelMap.addAttribute("lendingMap", lendingList);
    model.addAttribute("searchStr", searchStr);
    model.addAttribute("bookRepository", bookRepository);
    model.addAttribute("bookState_FREE", BookState.FREE);
    model.addAttribute("bookState_CART", BookState.CART);
    model.addAttribute("lendingState_CART", LendingState.CART);
    model.addAttribute("lendingState_RENTAL", LendingState.RENTAL);
    model.addAttribute("lendingState_RETURN", LendingState.RETURN);
    model.addAttribute("lendingState_CLOSE", LendingState.CLOSE);
  }

  /**
   * 本の数を数える
   * @param bookTitle 本のタイトル
   * @param bookCount 本の数を格納しているMap変数
   * @return キー：本のタイトル、値：本の数でMap型を返す
   */
  public LinkedHashMap<String, Integer>
  bookCounter(LinkedHashMap<String, Integer> bookCount, String bookTitle) {
    var bookArray = bookRepository.findByBookNameIdAndState(
        bookNameRepository.findByTitle(bookTitle).get(), BookState.FREE);
    int bookCountNum = 0;
    if (bookArray != null)
      bookCountNum = bookArray.size();

    bookCount.put(bookTitle, bookCountNum);
    return bookCount;
  }

  /**
   * 全ての本の数を数える
   * @param bookCount 本の数を格納しているMap変数
   * @return キー：本のタイトル、値：本の数でMap型を返す
   */
  public LinkedHashMap<String, Integer>
  allBookCounter(LinkedHashMap<String, Integer> bookCount) {
    var bookNames = bookNameRepository.findAll();
    for (var bookName : bookNames) {
      bookCount = bookCounter(bookCount, bookName.getTitle());

      System.out.println(bookName.getTitle() +
                         "の数: " + bookCount.get(bookName.getTitle()));
    }
    return bookCount;
  }

  /**
   * ログインユーザーの借りている本のMapを作成する
   * @param lendingList
   * @return
   */
  public LinkedHashMap<String, Lending>
  setAllLendingList(LinkedHashMap<String, Lending> lendingList) {
    Authentication loginUser =
        SecurityContextHolder.getContext().getAuthentication();
    var lendings = lendingRepository.findListByUser(
        userRepository.findByEmail(loginUser.getName()).get());
    for (var lending : lendings) {
      //キー：本のタイトル、値：lendingオブジェクト
      lendingList.put(lending.getBook().getBookNameId().getTitle(), lending);
      System.out.println("現在借りている本：" +
                         lending.getBook().getBookNameId().getTitle());
    }
    return lendingList;
  }

  /**
   * 部分一致で検索する関数
   * @param searchStr
   * @return ヒットした本のリストを返す。貸し出し不可の状態でも表示する
   */
  public ArrayList<BookName> findByTitleBookLike(String searchStr) {
    System.out.println("本のタイトル：" + searchStr);
    return bookNameRepository.findByTitleIgnoreCaseLikeOrderByTitleDesc(
        "%" + searchStr + "%");
    // try {

    // Authentication loginUser =
    //     SecurityContextHolder.getContext().getAuthentication();
    // var lendingIds = lendingRepository.findListByUser(
    //     userRepository.findByEmail(loginUser.getName()).get());
    // for (var lendingId : lendingIds) {
    //   System.out.println("lendingId" + lendingId);
    // }

    // 該当のある本のうち一つだけを返す
    // for (var bookName : bookNames) {
    // for (var lendingId : lendingIds) {
    // 借りている本(カートに入っている本)があるならその本のインスタンスを返す
    //   if (bookName.getId() ==
    //   lendingId.getBook().getBookNameId().getId()) {
    //     books.add(lendingId.getBook());
    //     break;
    //   }
    // 借りていないなら一番IDの数字が大きい本のインスタンスを返す(借りていない本のインスタンスのはず)

    // System.out.println(bookNameIdBook.getBookNameId().getTitle() + " :
    // " +
    //                    bookNameIdBook.getId());
    // if (lendingRepository.existsByBook(bookNameIdBook)) {
    //   System.out.println(
    //       "lendingRepository.existsByBook(bookNameIdBook:" +
    //       lendingRepository.existsByBook(bookNameIdBook));

    // } else {
    //
    // }
    // books.add(
    //     bookRepository.findTopByBookNameIdOrderByIdDesc(bookName).get());

    //ヒットした数の本の実態の分を代入している
    // var bookNameLists =
    //     bookNameRepository.findByTitleIgnoreCaseLikeOrderByTitleDesc(
    //         bookName);
    // for (var bookNameList : bookNameLists) {
    //   books.add(bookNameList);
    // }
    // }
    // return bookNames;
    // } catch (Exception e) {
    //   System.out.println(e.getCause() + " が findByTitleBookLike() で発生");
    //   return null;
    // }
  }

  /**
   * カートに入れるとき、取り出すとき同じモデルを定義しているのでまとめる
   * @param user
   * @param model
   * @param books
   * @param searchStr
   */
  public void setLendingModel(Authentication user, Model model,
                              ModelMap modelMap, ArrayList<BookName> bookName,
                              String searchStr) {

    // 各本の名前毎に数を数える
    var bookCount = new LinkedHashMap<String, Integer>();
    bookCount = allBookCounter(bookCount);

    // 現在の貸し借り状況をmapに格納する
    var lendingList = new LinkedHashMap<String, Lending>();
    lendingList = setAllLendingList(lendingList);

    model.addAttribute("bookList", bookName);
    model.addAttribute("bookNameList", bookNameRepository.findAll());
    var cartLendingList = lendingRepository.findListByUserAndState(
        userRepository.findByEmail(user.getName()).get(), LendingState.CART);
    model.addAttribute("cartLendingList", cartLendingList);
    var rentalList = lendingRepository.findListByUserAndState(
        userRepository.findByEmail(user.getName()).get(), LendingState.RENTAL);
    model.addAttribute("rentalList", rentalList);
    model.addAttribute("bookState_CART", BookState.CART);
    model.addAttribute("searchStr", searchStr);
    model.addAttribute("bookRepository", bookRepository);
    model.addAttribute("bookState_FREE", BookState.FREE);
    modelMap.addAttribute("bookCountMap", bookCount);
    modelMap.addAttribute("lendingMap", lendingList);
    model.addAttribute("lendingState_CART", LendingState.CART);
    model.addAttribute("lendingState_RENTAL", LendingState.RENTAL);
    model.addAttribute("lendingState_RETURN", LendingState.RETURN);
    model.addAttribute("lendingState_CLOSE", LendingState.CLOSE);
  }

  /**
   * カートのページから本を取り出すときの処理
   * @param user
   * @param model
   * @param books
   */
  public void setCartLendingModel(Authentication user, Model model) {
    model.addAttribute("bookNameList", bookNameRepository.findAll());
    var cartLendingList = lendingRepository.findListByUserAndState(
        userRepository.findByEmail(user.getName()).get(), LendingState.CART);
    model.addAttribute("cartLendingList", cartLendingList);
    var rentalList = lendingRepository.findListByUserAndState(
        userRepository.findByEmail(user.getName()).get(), LendingState.RENTAL);
    model.addAttribute("rentalList", rentalList);
    model.addAttribute("bookState_CART", BookState.CART);
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
