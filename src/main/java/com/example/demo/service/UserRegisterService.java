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

@RequiredArgsConstructor
@Service
public class UserRegisterService {
  @Autowired UserMngRepository userRepository;
  @Autowired LendingRepository lendingRepository;

  public User userSetCartLending(User user, Lending lend) {
    var lendings = user.getLendings();
    lendings.add(lend);
    user.setLendings(lendings);
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    user.setUpdated_at(timestamp);
    userRepository.save(user);

    return user;
  }

  public User userDeleteCartLending(User user, Lending lend) {
    var lendings = user.getLendings();
    lendings.remove(lend);
    user.setLendings(lendings);
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    user.setUpdated_at(timestamp);
    userRepository.save(user);

    return user;
  }

  public User userSetRentalLending(User user, List<Lending> lendings) {
    user.setLendings(lendings);

    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    user.setUpdated_at(timestamp);
    userRepository.save(user);

    return user;
  }

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

  public void deleteLendingRelationship(User user, Lending lend) {
    var lendings = user.getLendings();
    lendings.remove(lend.getId());
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    user.setUpdated_at(timestamp);
    userRepository.save(user);
  }
}
