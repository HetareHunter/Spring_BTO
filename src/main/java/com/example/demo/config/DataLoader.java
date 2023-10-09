package com.example.demo.config;

import com.example.demo.model.Book;
import com.example.demo.model.BookName;
import com.example.demo.model.Genre;
import com.example.demo.model.User;
import com.example.demo.repository.BookNameRepository;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.GenreRepository;
import com.example.demo.repository.UserMngRepository;
import com.example.demo.service.FileLoader;
import com.example.demo.util.Authority;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.Resource;
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
  private final GenreRepository genreRepository;
  private final Timestamp timestamp = new Timestamp(System.currentTimeMillis());
  @Autowired private FileLoader fileLoader;

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
    testUser.setName(testUser.getFirst_name() + " " + testUser.getLast_name());
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
                           "は同じメールアドレスが登録されています");
      }
    }
    userMngRepository.saveAll(registerUsers);
    for (User user : registerUsers) {
      System.out.println(user.getName() + " を登録しました");
    }
    genreInitRun();
    bookInitRun();
  }

  /**
   * bookエンティティの初期化を行う(暫定的に一つの本の名前に対して一つの本が存在することとしている)
   */
  void bookInitRun() {
    var books = new ArrayList<Book>();
    var bookId = 0;
    var bookNames = bookNameInitRun();
    for (BookName bookName : bookNames) {
      var book = new Book();
      book.setBookNameId(
          bookNameRepository.findByTitle(bookName.getTitle()).get());
      var bookStock =
          bookRepository.findByBookNameId(book.getBookNameId()).size();
      var booknameStock = bookName.getId();

      // とりあえずテスト用に本の個数は本の名前のIDの数字の数だけあることとする
      for (int i = 0; i < booknameStock - bookStock; i++) {
        book.setId(bookId);
        bookId++;
        book.setActive(true);
        book.setLendable(true);
        book.setCreated_at(timestamp);
        book.setUpdated_at(timestamp);
        books.add(book);
      }
      bookRepository.saveAll(books);
      books.clear();
    }
  }

  /**
   * BookNameデータを初期化する
   * @return
   */
  List<BookName> bookNameInitRun() {
    var bookNames = new ArrayList<BookName>();
    List<String> strArray = null;
    var resource = fileLoader.load("SpringBTO_data.csv");
    // ファイルを参照できなければそれ以降の処理は行わない
    strArray = CSVLoader(resource);
    if (strArray == null)
      return null;

    try {
      for (String bookNameStr : strArray) {
        var bookNameElements = bookNameStr.split(",");
        // for (int i = 0; i < bookNameElements.length; i++) {
        //   System.out.println(i + " : " + bookNameElements[i]);
        // }
        var bookName = new BookName();

        // 文字列のデータが正常に切り分けられていなければスキップする
        try {
          // IDが数字でなければ以降の処理もスキップする
          try {
            bookName.setId(Integer.parseInt(removeSpaces(bookNameElements[0])));
          } catch (NumberFormatException e) {
            System.err.println("IDを取得できません。スキップします" +
                               e.getMessage());
            continue;
          }
          bookName.setTitle(bookNameElements[1]);
          bookName.setAuthor(bookNameElements[2]);
          bookName.setDetail(bookNameElements[3]);
          bookName.setPublisher(bookNameElements[4]);
          bookName.setGenre(
              genreRepository.findByName(bookNameElements[5]).get());
          bookName.setImg(bookNameElements[6]);
          bookName.setActive(Boolean.valueOf(bookNameElements[7]));
          bookName.setNewName(Boolean.valueOf(bookNameElements[8]));
        } catch (ArrayIndexOutOfBoundsException e) {
          System.err.println(
              "正常にデータを取得できませんでした。スキップします" +
              e.getMessage());
          continue;
        }

        bookName.setCreated_at(timestamp);
        bookName.setUpdated_at(timestamp);
        bookNames.add(bookName);
      }
      bookNameRepository.saveAll(bookNames);
    } catch (Exception e) {
      System.out.println("ファイルの存在は：" + resource.exists() + "です");
      System.err.println("ファイルを参照できませんでした" + e.getMessage());
    }
    return bookNames;
  }

  void genreInitRun() {
    String[] names = {"なし", "技術書", "経済書", "雑誌"};
    // 存在する要素についてはnamesの名前になるように更新する
    for (int i = 0; i < names.length; i++) {
      if (genreRepository.existsById(i + 1)) {
        genreRepository.setQueryName(names[i], i + 1);
      } else {
        var genre = new Genre();
        genre.setId(i + 1);
        genre.setName(names[i]);
        genre.setCreated_at(timestamp);
        genre.setUpdated_at(timestamp);
        genreRepository.save(genre);
      }
    }
  }

  List<String> CSVLoader(Resource filePath) {
    String content = "";
    List<String> returnList = new ArrayList<>();
    try {
      InputStream inputStream = filePath.getInputStream();
      Scanner scanner = new Scanner(inputStream);
      scanner.useDelimiter("\\Z"); // ファイルの末尾まで読み込む
      if (scanner.hasNext()) {
        content = scanner.next();
      }
      scanner.close();
    } catch (IOException e) {
      System.err.println("Error reading file: " + e.getMessage());
    }
    returnList = Arrays.asList(content.split(",,", -1)); // 分割
    return returnList;
  }
  String removeSpaces(String input) { return input.replaceAll("[\\s　]", ""); }
}
