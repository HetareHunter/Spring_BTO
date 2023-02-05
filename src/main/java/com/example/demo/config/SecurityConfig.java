package com.example.demo.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.example.demo.repository.UserMngRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class SecurityConfig
{
	private final UserMngRepository userRepository;

	@Bean
	public PasswordEncoder passwordEncoder()
	{
		// パスワードの暗号化用にBCryptを使う
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception
	{
		// 認証リクエストの設定
		http.authorizeHttpRequests(auth -> auth
				// 認証の必要があるように設定
				.requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll().anyRequest()
				.authenticated())
				// フォームベースの認証
				.formLogin(login -> login.loginPage("/login").defaultSuccessUrl("/", true).permitAll())
				// ログアウトの設定
				.logout(logout -> logout.logoutRequestMatcher(new AntPathRequestMatcher("/logout")).permitAll())
				// Remember-Meの認証を許可する
				.rememberMe();
		return http.build();		
	}

	@Bean
	public UserDetailsService userDetailsService()
	{
//		//"user"を作成
//		UserDetails user = User.withUsername("user")
//				//"password"をBCryptで暗号化
//				.password(passwordEncoder().encode("password"))
//				//権限を設定
//				.authorities("USER").build();

		return username -> {
			// ユーザー名を検索し、存在しない場合は例外を投げる
			var user = userRepository.findByName(username)
					.orElseThrow(() -> new UsernameNotFoundException(username + "not Found"));

			return new User(user.getName(), user.getPassword(), AuthorityUtils.createAuthorityList("ADMIN"));
		};
		// メモリ内認証を使用
		// return new InMemoryUserDetailsManager(user);
	}
}
