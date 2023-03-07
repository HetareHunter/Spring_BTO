package com.example.demo.service;

import java.sql.Timestamp;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Lending;
import com.example.demo.model.User;
import com.example.demo.repository.LendingRepository;
import com.example.demo.repository.UserMngRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserRegisterService
{
	@Autowired
	UserMngRepository userRepository;
	@Autowired
	LendingRepository lendingRepository;

	public User userSetCartLending(User user, Lending lend)
	{
		var lendingMap = user.getLendings();
		var lendId = lend.getId();
		if (lendingMap.containsKey(lendId))
		{
			lendingMap.put(lendId, lend);
		}
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		user.setUpdated_at(timestamp);
		userRepository.save(user);

		return user;
	}

	public User userSetRentalLending(User user, Lending lend)
	{
		var lendingMap = user.getLendings();
		var lendId = lend.getId();
		if (lendingMap.containsKey(lendId))
		{
			lendingMap.replace(lendId, lend);
		}

		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		user.setUpdated_at(timestamp);
		userRepository.save(user);

		return user;
	}

	public void deleteAllLendingRelationship()
	{
		var users = userRepository.findAll();
		var lendings = new HashMap<Integer, Lending>();
		for (User user : users)
		{
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			user.setLendings(lendings);
			user.setUpdated_at(timestamp);
			userRepository.save(user);
		}
		System.out.println("全てのユーザーの本の貸し借り関係を削除した");
	}

	public void deleteLendingRelationship(User user, Lending lend)
	{
		var lendings = user.getLendings();
		lendings.remove(lend.getId());
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		user.setUpdated_at(timestamp);
		userRepository.save(user);
	}
}
