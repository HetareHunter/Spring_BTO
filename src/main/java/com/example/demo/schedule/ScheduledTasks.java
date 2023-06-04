package com.example.demo.schedule;

import com.example.demo.model.WeatherEntity;
import com.example.demo.repository.WeatherRepository;
import com.example.demo.service.weather.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * タスクスケジューラーを定義、設定するクラス
 */
@Component
public class ScheduledTasks {
  @Autowired WeatherService weatherService;
  @Autowired WeatherRepository weatherrepository;

  /**
   * 天気情報を定期的に更新するメソッド。現在は10分間隔
   */
  @Scheduled(fixedDelay = 600000)
  public void updateWeatherInfo() {
    WeatherEntity weatherEntity = new WeatherEntity();
    weatherEntity = weatherService.setWeatherInfo(weatherEntity);

    weatherrepository.save(weatherEntity);
    System.out.println("天気情報更新");
  }
}