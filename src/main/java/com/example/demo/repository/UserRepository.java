package com.example.demo.repository;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserRepository implements ApplicationRunner
{
	private final JdbcTemplate jdbcTemplate;

	//@formatter:off
//	public List<User> getAll()
//	{
//		String sql = "select id,name,email,state,password from test";
//		List<Map<String, Object>> userList = jdbcTemplate.queryForList(sql);
//		List<User> list = new ArrayList<>();
//		for(Map<String,Object> user : userList) {
//			list.add(new User(
//					(BigDecimal) user.get("id"),
//					(String) user.get("name"),
//					(String) user.get("password"),
//					(String) user.get("email"),
//					(String) user.get("state")
//					));
//		}
//		//System.out.println(list.get(0).getId());
//		return list;
//	}
	//@formatter:on
//	
//	public BigDecimal maxId() {
//		String sql = "select max(id) from test";
//		List<Map<String, Object>> userList = jdbcTemplate.queryForList(sql);
//		List<User> list = new ArrayList<>();
//		for(Map<String,Object> user : userList) {
//			list.add(new User(
//					(BigDecimal) user.get("id"),
//					(String) user.get("name"),
//					(String) user.get("password"),
//					(String) user.get("email"),
//					(String) user.get("state")
//					));
//		}
//		BigDecimal id = list.get(0).getId();
//		//System.out.println(id);
//		return id;
//	}

	@Override
	public void run(ApplicationArguments args) throws Exception
	{
		// TODO 自動生成されたメソッド・スタブ

	}
}
