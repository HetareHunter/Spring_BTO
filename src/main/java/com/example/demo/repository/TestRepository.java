package com.example.demo.repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.demo.model.User;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class TestRepository implements ApplicationRunner
{
	private final JdbcTemplate jdbcTemplate;

	public List<User> getAll()
	{
		String sql = "select id,name,email,state,password from test";
		List<Map<String, Object>> userList = jdbcTemplate.queryForList(sql);
		List<User> list = new ArrayList<>();
		for(Map<String,Object> user : userList) {
			list.add(new User(
					(BigDecimal) user.get("id"),
					(String) user.get("name"),
					(String) user.get("password"),
					(String) user.get("email"),
					(String) user.get("state")
					));
		}
		return list;
	}

	@Override
	public void run(ApplicationArguments args) throws Exception
	{
		// TODO 自動生成されたメソッド・スタブ
		
	}
}
