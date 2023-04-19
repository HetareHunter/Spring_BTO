package com.example.demo.config;

import com.example.demo.model.Book;
import com.example.demo.model.BookName;
import com.example.demo.model.Genre;
import com.example.demo.model.User;
import com.example.demo.repository.BookNameRepository;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.GenreRepository;
import com.example.demo.repository.LendingRepository;
import com.example.demo.repository.UserMngRepository;
import com.example.demo.util.Authority;
import java.sql.Timestamp;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class DataLoader implements ApplicationRunner {
  private final PasswordEncoder passwordEncoder;
  private final UserMngRepository userMngRepository;
  private final BookRepository bookRepository;
  private final BookNameRepository bookNameRepository;
  private final LendingRepository lendingRepository;
  private final GenreRepository genreRepository;

  private String setPreGenre = "技術書";
  private String setPreGenre2 = "経済書";

  @Override
  public void run(ApplicationArguments args) throws Exception {
    var users = new ArrayList<User>();

    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
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

    var registerUsers = new ArrayList<User>();
    for (User user : users) {
      user.setCreated_at(timestamp);
      user.setUpdated_at(timestamp);
      if (userMngRepository.findByEmail(user.getEmail()).isEmpty()) {
        registerUsers.add(user);
      }
    }
    userMngRepository.saveAll(registerUsers);
    for (User user : registerUsers) {
      System.out.println(user.getName() + " を登録しました");
    }

    lendingInitRun();
    genreInitRun();
    bookInitRun();
  }

  void bookInitRun() {
    bookRepository.deleteAll();
    var books = new ArrayList<Book>();
    // bookNameInitRun1();
    // bookNameInitRun2();
    var bookNames = bookNameRepository.findAll();
    for (BookName bookName : bookNames) {
      var book = new Book();
      book.setActive(true);
      book.setLendable(true);
      book.setBookNameId(
          bookNameRepository.findByTitle(bookName.getTitle()).get());
      Timestamp timestamp = new Timestamp(System.currentTimeMillis());
      book.setCreated_at(timestamp);
      book.setUpdated_at(timestamp);
      books.add(book);
    }
    bookRepository.saveAll(books);
  }

  void lendingInitRun() { lendingRepository.findAll(); }

  void genreInitRun() {
    var genre = new Genre();
    genre.setName("なし");

    if (genreRepository.findByName(genre.getName()).isEmpty()) {
      Timestamp timestamp = new Timestamp(System.currentTimeMillis());
      genre.setCreated_at(timestamp);
      genre.setUpdated_at(timestamp);
      genreRepository.save(genre);
    }

    var genre1 = new Genre();
    genre1.setName(setPreGenre);

    if (genreRepository.findByName(genre1.getName()).isEmpty()) {
      Timestamp timestamp = new Timestamp(System.currentTimeMillis());
      genre1.setCreated_at(timestamp);
      genre1.setUpdated_at(timestamp);
      genreRepository.save(genre1);
    }

    var genre2 = new Genre();
    genre2.setName(setPreGenre2);
    if (genreRepository.findByName(genre2.getName()).isEmpty()) {
      Timestamp timestamp = new Timestamp(System.currentTimeMillis());
      genre2.setCreated_at(timestamp);
      genre2.setUpdated_at(timestamp);
      genreRepository.save(genre2);
    }
  }
}
