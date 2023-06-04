package com.example.demo.service;

import com.example.demo.model.Book;
import com.example.demo.model.Lending;
import com.example.demo.model.User;
import com.example.demo.repository.LendingRepository;
import com.example.demo.util.LendingState;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LendingService {
  @Autowired private LendingRepository lendingRepository;
  // 貸し出し期間
  private int lendablePeriod = 14;
  // カートに入れて保存されている時間
  private int tempLendablePeriod = 1;
  // 貸し出し期間
  private Calendar calendar;

  /**
   * ショッピングサイトのカートに保存するときの貸し借り状態を保存する処理
   * @param book
   * @param user
   * @return
   */
  public Lending setLendingCart(Book book, User user) {
    Lending lend = new Lending();
    lend.setBook(book);
    lend.setUser(user);

    calendar = Calendar.getInstance();

    lend.setLendDate(new Date(calendar.getTimeInMillis())); // 借りた日をset

    // 返す日は借りた日にlendablePeriodを加えた日付とする
    calendar.add(Calendar.DATE, tempLendablePeriod);
    lend.setReturnDueDate(new Date(calendar.getTimeInMillis())); // 返す日をset

    // 返した日はまだ設定できない
    lend.setReturnDate(null);

    // 延滞日数のカウントは登録日にはできないので暫定的に0を入れている
    lend.setOverdueDate(0);
    lend.setState(LendingState.CART);

    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    lend.setCreated_at(timestamp);
    lend.setUpdated_at(timestamp);
    lendingRepository.save(lend);
    return lend;
  }

  /**
   * 正式に借りるときの処理
   * @param lendings
   * @return
   */
  public List<Lending> setLendingRental(List<Lending> lendings) {
    for (var lend : lendings) {
      calendar = Calendar.getInstance();

      lend.setLendDate(new Date(calendar.getTimeInMillis())); // 借りた日をset

      // 返す日は借りた日にlendablePeriodを加えた日付とする
      calendar.add(Calendar.DATE, lendablePeriod);
      lend.setReturnDueDate(
          new Date(calendar.getTimeInMillis())); // 返す日をset

      // 返した日はまだ設定できない
      lend.setReturnDate(null);

      // 延滞日数のカウントは登録日にはできないので暫定的に0を入れている
      lend.setOverdueDate(0);
      lend.setState(LendingState.RENTAL);

      Timestamp timestamp = new Timestamp(System.currentTimeMillis());
      lend.setUpdated_at(timestamp);

      System.out.println(lend.getBook().getBookNameId().getTitle() +
                         " を登録した");
    }
    lendingRepository.saveAll(lendings);

    return lendings;
  }

  /**
   * ユーザーが返却処理を行ったとき
   * @param lendings
   * @return
   */
  public List<Lending> setLendingReturn(List<Lending> lendings) {
    for (var lend : lendings) {
      calendar = Calendar.getInstance();

      // 返した日
      lend.setReturnDate(new Date(calendar.getTimeInMillis()));

      lend.setState(LendingState.RETURN);

      Timestamp timestamp = new Timestamp(System.currentTimeMillis());
      lend.setUpdated_at(timestamp);

      System.out.println(lend.getBook().getBookNameId().getTitle() +
                         " を返却処理した");
    }
    lendingRepository.saveAll(lendings);

    return lendings;
  }

  /**
   * 管理者が返却を複数受理したとき
   * @param lendings
   * @return
   */
  public List<Lending> setLendingClose(List<Lending> lendings) {
    for (var lend : lendings) {
      calendar = Calendar.getInstance();

      // 返した日
      lend.setReturnDate(new Date(calendar.getTimeInMillis()));

      lend.setState(LendingState.CLOSE);

      Timestamp timestamp = new Timestamp(System.currentTimeMillis());
      lend.setUpdated_at(timestamp);

      System.out.println(lend.getBook().getBookNameId().getTitle() +
                         " を返却を受理しクローズした");
    }
    lendingRepository.saveAll(lendings);
    return lendings;
  }

  /**
   * 管理者が返却を一つ受理したとき
   * @param lendings
   * @return
   */
  public Lending setLendingClose(Lending lend) {
    calendar = Calendar.getInstance();

    // 返した日
    lend.setReturnDate(new Date(calendar.getTimeInMillis()));

    lend.setState(LendingState.CLOSE);

    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    lend.setUpdated_at(timestamp);

    System.out.println(lend.getBook().getBookNameId().getTitle() +
                       " を返却を受理しクローズした");
    lendingRepository.save(lend);
    return lend;
  }

  /**
   * 対象の貸し借りの状態を削除する
   * @param lendId
   */
  public void deleteLending(int lendId) {
    lendingRepository.deleteById(lendId);
    System.out.println("lendId " + lendId +
                       " のLendingオブジェクトを削除しました");
  }

  /**
   * 貸し借りの状態を全て削除する
   */
  public void deleteAllLending() {
    lendingRepository.deleteAll();
    System.out.println("全てのLendingオブジェクトを削除しました");
  }
}
