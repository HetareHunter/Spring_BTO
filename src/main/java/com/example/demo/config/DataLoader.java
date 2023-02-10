package com.example.demo.config;

import java.sql.Timestamp;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.demo.model.User;
import com.example.demo.repository.UserMngRepository;
import com.example.demo.util.Authority;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class DataLoader implements ApplicationRunner
{
	private final PasswordEncoder passwordEncoder;
	private final UserMngRepository userMngRepository;

	@Override
	public void run(ApplicationArguments args) throws Exception
	{
		var user = new User();
		user.setFirst_name("かつまた");
		user.setLast_name("管理人");
		user.setEmail("admin@example");
		user.setPassword(passwordEncoder.encode("password"));
		user.setRole(Authority.ADMIN);
		user.setName(user.getFirst_name() + " " + user.getLast_name());

		user.setAdmin(true);

		System.out.println(user.getFirst_name() + " " + user.getLast_name());
		System.out.println(userMngRepository.findByEmail(user.getEmail()));
		if (userMngRepository.findByEmail(user.getEmail()).isEmpty())
		{
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			user.setCreated_at(timestamp);
			user.setUpdated_at(timestamp);
			userMngRepository.save(user);
			System.out.println(user.getFirst_name() + " " + user.getLast_name() + " を登録しました");
		}
	}
}
