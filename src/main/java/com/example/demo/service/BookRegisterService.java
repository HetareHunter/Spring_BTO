package com.example.demo.service;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.example.demo.model.Book;
import com.example.demo.model.BookName;
import com.example.demo.repository.BookNameRepository;
import com.example.demo.repository.BookRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class BookRegisterService
{
	@Autowired
	private BookRepository bookRepository;
	@Autowired
	private BookNameRepository bookNameRepository;

	public void bookSave(Book book, BookName bookName, Model model)
	{
		book.setActive(true);
		book.setBookNameId(bookNameRepository.findById(bookName.getId()).get());
//		if (bookName.isActive())
//		{
//			book.setLendable(true);
//		} else
//		{
//			book.setLendable(false);
//		}
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		book.setCreated_at(timestamp);
		book.setUpdated_at(timestamp);
		bookRepository.save(book);
		System.out.println(bookName.getTitle() + " を登録した");
	}
}
