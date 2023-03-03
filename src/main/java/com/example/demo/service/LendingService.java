package com.example.demo.service;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.example.demo.model.Book;
import com.example.demo.model.Lending;
import com.example.demo.model.User;
import com.example.demo.repository.LendingRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class LendingService
{
	@Autowired
	private LendingRepository lendingRepository;
	//貸し出し期間
	private int lendablePeriod = 14;

	
	public void LendingSave(Lending lend, Book book, User user, Model model)
	{
		lend.setBook(book);
		lend.setUser(user);		
		
		Date currentDate = new Date(System.currentTimeMillis());
		lend.setLendDate(currentDate);
		
		Date futureDate = Date.valueOf(LocalDate.now().plusDays(lendablePeriod));
		lend.setReturnDueDate(futureDate);
		
		lend.setReturnDate(null);		

		LocalDate currentLocalDate = currentDate.toLocalDate();
		LocalDate futureLocalDate = futureDate.toLocalDate();

		int daysDiff = (int)ChronoUnit.DAYS.between(currentLocalDate, futureLocalDate);		
		lend.setOverdueDate(daysDiff);
		
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		lend.setCreated_at(timestamp);
		lend.setUpdated_at(timestamp);
		lendingRepository.save(lend);
		System.out.println(lend.getBook().getBookNameId().getTitle() + " を登録した");
	}
}
