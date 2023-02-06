package com.example.demo.config;

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
		user.setName("管理者");
		user.setEmail("admin@example");
		user.setAuthority(Authority.ADMIN);
		user.setPassword(passwordEncoder.encode("password"));
		user.setUserID("admin");
		user.setAdmin(true);

		System.out.println(user.getName());
		System.out.println(userMngRepository.findByName(user.getName()));
		if (userMngRepository.findByName(user.getName()).isEmpty())
		{
			userMngRepository.save(user);
			System.out.println(user.getName()+" を登録しました");
		}
	}
}
