package com.example.demo.service;

import com.example.demo.model.Lending;
import com.example.demo.model.User;
import com.example.demo.repository.LendingRepository;
import com.example.demo.repository.UserMngRepository;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * ユーザーの貸し借り状態の変更処理
 */
@RequiredArgsConstructor
@Service
public class UserLendingService {
  @Autowired UserMngRepository userRepository;
  @Autowired LendingRepository lendingRepository;

  /**
   * カートに入れたときのユーザーの貸し借り状態設定
   * @param user
   * @param lend
   * @return
   */
  public User userSetCartLending(User user, Lending lend) {
    var lendings = user.getLendings();
    lendings.add(lend);
    user.setLendings(lendings);
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    user.setUpdated_at(timestamp);
    userRepository.save(user);

    return user;
  }

  /**
   * カートから取り出したときのユーザーの貸し借り状態設定
   * @param user
   * @param lend
   * @return
   */
  public User userDeleteCartLending(User user, Lending lend) {
    var lendings = user.getLendings();
    lendings.remove(lend);
    user.setLendings(lendings);
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    user.setUpdated_at(timestamp);
    userRepository.save(user);

    return user;
  }

  /**
   * 借り入れたときのユーザーの貸し借り状態設定
   * @param user
   * @param lendings
   * @return
   */
  public User userSetRentalLending(User user, List<Lending> lendings) {
    user.setLendings(lendings);

    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    user.setUpdated_at(timestamp);
    userRepository.save(user);

    return user;
  }

  /**
   * 全てのユーザーの本の貸し借り関係を削除したときの処理
   */
  public void deleteAllLendingRelationship() {
    var users = userRepository.findAll();
    var lendings = new ArrayList<Lending>();
    for (User user : users) {
      Timestamp timestamp = new Timestamp(System.currentTimeMillis());
      user.setLendings(lendings);
      user.setUpdated_at(timestamp);
      userRepository.save(user);
    }
    System.out.println("全てのユーザーの本の貸し借り関係を削除した");
  }

  /**
   * 特定のユーザーの本の貸し借り関係を削除したときの処理
   * @param user
   * @param lend
   */
  public void deleteLendingRelationship(User user, Lending lend) {
    var lendings = user.getLendings();
    lendings.remove(lend.getId());
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    user.setUpdated_at(timestamp);
    userRepository.save(user);
  }
}
