package com.example.demo.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class BookRentalService
{
//	@Autowired
//	private BookRepository bookRepository;
//	@Autowired
//	private LendingRepository lendingRepository;

	@Autowired
	Map<Integer, Integer> bookSelected;

	public Map<Integer, Integer> selectedBook(int userId, int bookId)
	{
		bookSelected.put(userId, bookId);
		return bookSelected;
	}
}
