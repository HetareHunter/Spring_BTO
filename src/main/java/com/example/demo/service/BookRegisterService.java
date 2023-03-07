package com.example.demo.service;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Book;
import com.example.demo.model.BookName;
import com.example.demo.repository.BookNameRepository;
import com.example.demo.repository.BookRepository;
import com.example.demo.util.BookState;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class BookRegisterService
{
	@Autowired
	private BookRepository bookRepository;
	@Autowired
	private BookNameRepository bookNameRepository;

	public void bookSave(Book book, BookName bookName)
	{
		book.setActive(true);
		book.setBookNameId(bookNameRepository.findById(bookName.getId()).get());
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		book.setLendable(true);
		book.setState(BookState.FREE);
		book.setCreated_at(timestamp);
		book.setUpdated_at(timestamp);
		bookRepository.save(book);
		System.out.println(bookName.getTitle() + " を登録した");
	}

	public void bookLendableChange(Book book, boolean lendable, BookState state)
	{
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		book.setLendable(lendable);
		book.setState(state);
		book.setUpdated_at(timestamp);
		bookRepository.save(book);
		System.out.println(book.getBookNameId().getTitle() + " の貸し出しを " + lendable + " にした");
	}

	public void bookCartSave(Book book)
	{
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		book.setLendable(false);
		book.setState(BookState.CART);
		book.setUpdated_at(timestamp);
		bookRepository.save(book);
		System.out.println(book.getBookNameId().getTitle() + " をカートに入れた");
	}

	public void bookRentalSave(Book book)
	{
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		book.setLendable(false);
		book.setState(BookState.RENTAL);
		book.setUpdated_at(timestamp);
		bookRepository.save(book);
		System.out.println(book.getBookNameId().getTitle() + " を貸し出した");
	}

	public void bookAllLendableChange(boolean lendable)
	{
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		var books = bookRepository.findAll();
		for (Book book : books)
		{
			book.setLendable(lendable);
			book.setState(BookState.FREE);
			book.setUpdated_at(timestamp);
			bookRepository.save(book);
		}
		System.out.println("全ての本の貸し出しを " + lendable + " にした");
	}
}
