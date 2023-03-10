package com.example.demo.service;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Book;
import com.example.demo.model.Lending;
import com.example.demo.model.User;
import com.example.demo.repository.LendingRepository;
import com.example.demo.util.LendingState;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class LendingService
{
	@Autowired
	private LendingRepository lendingRepository;
	// 貸し出し期間
	private int lendablePeriod = 14;
	// カートに入れて保存されている時間
	private int tempLendablePeriod = 1;
	// 貸し出し期間
	private Calendar calendar;
	
	// ショッピングサイトのカートに保存するときの処理
	public Lending setLendingCart(Book book, User user)
	{
		Lending lend = new Lending();
		lend.setBook(book);
		lend.setUser(user);

		calendar = Calendar.getInstance();

		lend.setLendDate(new Date(calendar.getTimeInMillis())); // 借りた日をset

		// 返す日は借りた日にlendablePeriodを加えた日付とする
		calendar.add(Calendar.DATE, tempLendablePeriod);
		lend.setReturnDueDate(new Date(calendar.getTimeInMillis())); // 返す日をset

		// 返した日はまだ設定できない
		lend.setReturnDate(null);

		// 延滞日数のカウントは登録日にはできないので暫定的に0を入れている
		lend.setOverdueDate(0);
		lend.setState(LendingState.CART);

		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		lend.setCreated_at(timestamp);
		lend.setUpdated_at(timestamp);
		lendingRepository.save(lend);
//		if (lendingRepository.findByBook(book).isEmpty())
//		{
//			lendingRepository.save(lend);
//			System.out.println(lend.getBook().getBookNameId().getTitle() + " の本をLendingテーブルに登録した");
//		} else
//		{
//			System.out.println(lend.getBook().getBookNameId().getTitle() + " の本は既に登録されています。登録失敗");
//		}

		return lend;
	}


	// 正式に借りるときの処理
	public List<Lending> setLendingRental(List<Lending> lendings)
	{
		for(var lend:lendings) {
			calendar = Calendar.getInstance();

			lend.setLendDate(new Date(calendar.getTimeInMillis())); // 借りた日をset

			// 返す日は借りた日にlendablePeriodを加えた日付とする
			calendar.add(Calendar.DATE, lendablePeriod);
			lend.setReturnDueDate(new Date(calendar.getTimeInMillis())); // 返す日をset

			// 返した日はまだ設定できない
			lend.setReturnDate(null);

			// 延滞日数のカウントは登録日にはできないので暫定的に0を入れている
			lend.setOverdueDate(0);
			lend.setState(LendingState.RENTAL);

			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			lend.setUpdated_at(timestamp);
			
			System.out.println(lend.getBook().getBookNameId().getTitle() + " を登録した");
		}
		lendingRepository.saveAll(lendings);

		return lendings;
	}
	
	//ユーザーが返却処理を行ったとき
	public List<Lending> setLendingReturn(List<Lending> lendings)
	{
		for(var lend:lendings) {
			calendar = Calendar.getInstance();

			// 返した日
			lend.setReturnDate(new Date(calendar.getTimeInMillis()));
			
			lend.setState(LendingState.RETURN);

			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			lend.setUpdated_at(timestamp);
			
			System.out.println(lend.getBook().getBookNameId().getTitle() + " を返却処理した");
		}
		lendingRepository.saveAll(lendings);

		return lendings;
	}
	
	//ユーザーが返却処理を行ったとき
	public List<Lending> setLendingClose(List<Lending> lendings)
	{
		for(var lend:lendings) {
			calendar = Calendar.getInstance();

			// 返した日
			lend.setReturnDate(new Date(calendar.getTimeInMillis()));
			
			lend.setState(LendingState.CLOSE);

			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			lend.setUpdated_at(timestamp);
			
			System.out.println(lend.getBook().getBookNameId().getTitle() + " を返却を受理しクローズした");
		}
		lendingRepository.saveAll(lendings);
		return lendings;
	}
	
	//ユーザーが返却処理を行ったとき
		public Lending setLendingClose(Lending lend)
		{
			calendar = Calendar.getInstance();

			// 返した日
			lend.setReturnDate(new Date(calendar.getTimeInMillis()));
			
			lend.setState(LendingState.CLOSE);

			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			lend.setUpdated_at(timestamp);
			
			System.out.println(lend.getBook().getBookNameId().getTitle() + " を返却を受理しクローズした");
			lendingRepository.save(lend);
			return lend;
		}
	
	public void deleteLending(int lendId)
	{
		lendingRepository.deleteById(lendId);
		System.out.println("lendId " + lendId + " のLendingオブジェクトを削除しました");
	}

	public void deleteAllLending()
	{
		lendingRepository.deleteAll();
		System.out.println("全てのLendingオブジェクトを削除しました");
	}
}
