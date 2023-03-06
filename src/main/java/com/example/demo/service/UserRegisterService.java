package com.example.demo.service;

import java.sql.Timestamp;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Lending;
import com.example.demo.model.User;
import com.example.demo.repository.UserMngRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserRegisterService
{
	@Autowired
	UserMngRepository userRepository;

	public User changeUserLending(User user, Lending lend)
	{
		var lendingList = user.getLendings();
		lendingList.add(lend);
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		user.setUpdated_at(timestamp);
		userRepository.save(user);

		return user;
	}

	public void deleteAllLendingRelationship()
	{
		var users = userRepository.findAll();
		var lendings = new ArrayList<Lending>();
		for (User user : users)
		{
			user.setLendings(lendings);
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			user.setUpdated_at(timestamp);
			userRepository.save(user);
		}
		System.out.println("全てのユーザーの本の貸し借り関係を削除した");
	}

	public void deleteLendingRelationship(User user, Lending lend)
	{
		var lendings = user.getLendings();
		lendings.remove(lend);
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		user.setUpdated_at(timestamp);
		userRepository.save(user);
	}
}
