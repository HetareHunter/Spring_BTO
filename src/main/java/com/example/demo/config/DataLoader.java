package com.example.demo.config;

import java.sql.Timestamp;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.demo.model.Book;
import com.example.demo.model.BookName;
import com.example.demo.model.Genre;
import com.example.demo.model.User;
import com.example.demo.repository.BookNameRepository;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.GenreRepository;
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
	private final BookNameRepository bookNameRepository;
	private final LendingRepository lendingRepository;
	private final GenreRepository genreRepository;

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
		lendingInitRun();

		if (userMngRepository.findByEmail(user.getEmail()).isEmpty())
		{
			System.out.println("if文到達");
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			user.setCreated_at(timestamp);
			user.setUpdated_at(timestamp);
			userMngRepository.save(user);
			System.out.println(user.getFirst_name() + " " + user.getLast_name() + " を登録しました");
		}
		bookInitRun();
		GenreInitRun();
	}

	void bookInitRun()
	{
		var book = new Book();
		book.setActive(true);
		book.setLendable(true);
		var bookName = bookNameInitRun();
		book.setBookNameId(bookNameRepository.findByTitle(bookName.getTitle()).get());
		System.out.println("bookRepository.save到達");
		if (bookRepository.findByBookNameId(bookNameRepository.findByTitle(bookName.getTitle())).isEmpty())
		{
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			book.setCreated_at(timestamp);
			book.setUpdated_at(timestamp);

			bookRepository.save(book);
		}
	}

	BookName bookNameInitRun()
	{
		var bookName = new BookName();
		bookName.setTitle("test");
		bookName.setAuthor("testAuthor");
		bookName.setDetail("testDetail");
		bookName.setPublisher("testPublisher");
		bookName.setGenre(1);
		bookName.setImg("testImg");
		bookName.setActive(true);
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		bookName.setCreated_at(timestamp);
		bookName.setUpdated_at(timestamp);
		System.out.println("bookNameInitRun save到達");
		System.out.println("bookName.id:" + bookName.getId());

		if (bookNameRepository.findByTitle(bookName.getTitle()).isEmpty())
		{

			bookNameRepository.save(bookName);
		}
		System.out.println("bookNameInitRun save完了");
		return bookName;
	}

	void lendingInitRun()
	{
//		var lending = new Lending();
//		lending.setUser(userMngRepository.findById(401).get());
//		//book.setLending(new ArrayList<>());
//		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
//		lending.setCreated_at(timestamp);
//		lending.setUpdated_at(timestamp);
//		lendingRepository.save(lending);

		lendingRepository.findAll();
	}

	void GenreInitRun()
	{
		var genre = new Genre();
		genre.setName("技術書");
		
		if (genreRepository.findByName(genre.getName()).isEmpty())
		{
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			genre.setCreated_at(timestamp);
			genre.setUpdated_at(timestamp);
			genreRepository.save(genre);
		}
		
		var genre2 = new Genre();
		genre2.setName("経済書");
		if (genreRepository.findByName(genre2.getName()).isEmpty())
		{
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			genre.setCreated_at(timestamp);
			genre.setUpdated_at(timestamp);
			genreRepository.save(genre2);
		}
	}
}
