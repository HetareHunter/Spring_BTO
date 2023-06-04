package com.example.demo.config;

import com.example.demo.model.Book;
import com.example.demo.model.BookName;
import com.example.demo.model.User;
import com.example.demo.repository.BookNameRepository;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.UserMngRepository;
import com.example.demo.util.Authority;
import java.sql.Timestamp;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * ApplicationRunnerインターフェースを実装しているので
 * アプリケーション起動前に実行される。run関数でoverrideしている
 */
@RequiredArgsConstructor
@Component
public class DataLoader implements ApplicationRunner {
  private final PasswordEncoder passwordEncoder;
  private final UserMngRepository userMngRepository;
  private final BookRepository bookRepository;
  private final BookNameRepository bookNameRepository;

  /**
   * userエンティティのspringのパスワードエンコーダーで暗号化するためここで初期設定をする
   * bookエンティティはbooknameエンティティを基に全名前毎一つずつ実態を作るためここで初期設定をする(暫定)
   * その他のモデルのエンティティはmodel.sqlでアプリケーション起動時に初期設定を行う
   */
  @Override
  public void run(ApplicationArguments args) throws Exception {
    var users = new ArrayList<User>();

    // タイムスタンプをを現在時刻とする
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());

    // 管理者用アカウントを設定
    var adminUser = new User();
    adminUser.setFirst_name("かつまた");
    adminUser.setLast_name("管理人");
    adminUser.setEmail("admin@example");
    adminUser.setPassword(passwordEncoder.encode("password"));
    adminUser.setRole(Authority.ADMIN);
    adminUser.setName(adminUser.getFirst_name() + " " +
                      adminUser.getLast_name());
    adminUser.setAdmin(true);
    users.add(adminUser);

    System.out.println(adminUser.getName());

    // ユーザー用テストアカウントを設定
    var testUser = new User();
    testUser.setFirst_name("Test");
    testUser.setLast_name("Account");
    testUser.setEmail("test@example");
    testUser.setPassword(passwordEncoder.encode("test"));
    testUser.setRole(Authority.USER);
    testUser.setName(adminUser.getFirst_name() + " " +
                     adminUser.getLast_name());
    testUser.setAdmin(false);
    users.add(testUser);

    // 登録する必要のあるユーザーがいる場合にこの変数に追加していく
    var registerUsers = new ArrayList<User>();
    for (User user : users) {
      // ここでユーザーデータを追加するときにタイムスタンプをまとめて設定する
      user.setCreated_at(timestamp);
      user.setUpdated_at(timestamp);
      // 同じメールアドレス(ログインID)が存在しない場合に追加する
      if (userMngRepository.findByEmail(user.getEmail()).isEmpty()) {
        registerUsers.add(user);
      } else {
        System.out.println(user.getEmail() +
                           "は同じメールアドレスが登録されています。");
      }
    }
    userMngRepository.saveAll(registerUsers);
    for (User user : registerUsers) {
      System.out.println(user.getName() + " を登録しました");
    }

    bookInitRun();
  }

  /**
   * bookエンティティの初期化を行う(暫定的に一つの本の名前に対して一つの本が存在することとしている)
   */
  void bookInitRun() {
    var books = new ArrayList<Book>();
    var bookNames = bookNameRepository.findAll();
    var timestamp = new Timestamp(System.currentTimeMillis());

    for (BookName bookName : bookNames) {
      var book = new Book();
      book.setBookNameId(
          bookNameRepository.findByTitle(bookName.getTitle()).get());
      // 同じ本の名前を参照する本(bookエンティティ)がある場合は飛ばす
      if (!bookRepository.findByBookNameId(book.getBookNameId()).isEmpty()) {
        System.out.println(bookName.getTitle() +
                           "は同じ本が登録されています。");
        continue;
      }

      book.setActive(true);
      book.setLendable(true);

      book.setCreated_at(timestamp);
      book.setUpdated_at(timestamp);
      books.add(book);
    }
    bookRepository.saveAll(books);
  }
}
