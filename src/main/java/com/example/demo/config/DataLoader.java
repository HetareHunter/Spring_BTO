package com.example.demo.config;

import java.sql.Timestamp;
import java.util.ArrayList;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.demo.model.Book;
import com.example.demo.model.Lending;
import com.example.demo.model.User;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.LendingRepository;
import com.example.demo.repository.UserMngRepository;
import com.example.demo.util.Authority;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class DataLoader implements ApplicationRunner
{
	private final PasswordEncoder passwordEncoder;
	private final UserMngRepository userMngRepository;
	private final BookRepository bookRepository;
	private final LendingRepository lendingRepository;

	@Override
	public void run(ApplicationArguments args) throws Exception
	{
		//BookInitRun();
		
		var user = new User();
		user.setFirst_name("かつまた");
		user.setLast_name("管理人");
		user.setEmail("admin@example");
		user.setPassword(passwordEncoder.encode("password"));
		user.setRole(Authority.ADMIN);
		user.setName(user.getFirst_name() + " " + user.getLast_name());
		user.setLending(new ArrayList<>());
		user.setAdmin(true);

		
		System.out.println(user.getFirst_name() + " " + user.getLast_name());
		//System.out.println(userMngRepository.findByEmail(user.getEmail()));
		LendingInitRun(user);
		if (userMngRepository.findByEmail(user.getEmail()).isEmpty())
		{
			System.out.println("if文到達");
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			user.setCreated_at(timestamp);
			user.setUpdated_at(timestamp);
			
			
			
			userMngRepository.save(user);
			System.out.println(user.getFirst_name() + " " + user.getLast_name() + " を登録しました");
		}
		
	}

	void BookInitRun()
	{
		var book = new Book();
		book.setActive(false);
		book.setLendable(false);
		//book.setLending(new ArrayList<>());
		if (bookRepository.findById(book.getId()).isEmpty())
		{
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			book.setCreated_at(timestamp);
			book.setUpdated_at(timestamp);
			bookRepository.save(book);
		}
	}
	
	void LendingInitRun(User user)
	{
		var lending = new Lending();
		lending.setUser(userMngRepository.findById(401).get());
		//book.setLending(new ArrayList<>());
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		lending.setCreated_at(timestamp);
		lending.setUpdated_at(timestamp);
		lendingRepository.save(lending);
	}
}
