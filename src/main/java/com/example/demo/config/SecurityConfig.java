package com.example.demo.config;

import com.example.demo.util.Authority;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@RequiredArgsConstructor
@Configuration
public class SecurityConfig {
  @Bean
  public PasswordEncoder passwordEncoder() {
    // パスワードの暗号化用にBCryptを使う
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    // 認証リクエストの設定
    http.authorizeHttpRequests(
            auth
            -> auth
                   // 認証の必要があるように設定
                   .requestMatchers(
                       PathRequest.toStaticResources().atCommonLocations())
                   .permitAll()
                   .requestMatchers("/register", "/login", "/confirm",
                                    "/complete", "/", "/logout")
                   .permitAll()
                   .requestMatchers("/admin.**")
                   .hasAuthority(Authority.ADMIN.name())
                   .anyRequest()
                   .authenticated())
        // フォームベースの認証
        .formLogin(login
                   -> login.loginPage("/login")
                          .defaultSuccessUrl("/", true)
                          .permitAll())
        // ログアウトの設定
        .logout(
            logout
            -> logout.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                   .logoutSuccessUrl("/")
                   .permitAll())
        // Remember-Meの認証を許可する
        .rememberMe()
        .key("katsu");
    return http.build();
  }
}
