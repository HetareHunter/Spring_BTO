package com.example.demo.service;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.repository.UserMngRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService
{
	private final UserMngRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException
	{
		// ユーザーIDを検索し、存在しない場合は例外を投げる
		var user = userRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException(email + "not Found"));

		return new User(user.getEmail(), user.getPassword(),
				AuthorityUtils.createAuthorityList(user.getRole().name()));
	}
}
