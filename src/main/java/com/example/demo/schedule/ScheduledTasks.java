package com.example.demo.schedule;

import com.example.demo.model.WeatherEntity;
import com.example.demo.repository.WeatherRepository;
import com.example.demo.service.weather.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {
  /**
   * 天気情報を定期的に更新するメソッド。現在は5分間隔
   */
  @Autowired WeatherService weatherService;
  @Autowired WeatherRepository weatherrepository;
  @Scheduled(fixedDelay = 300000)
  public void updateWeatherInfo() {
    WeatherEntity weatherEntity = new WeatherEntity();
    weatherEntity = weatherService.setWeatherInfo(weatherEntity);

    weatherrepository.save(weatherEntity);
    System.out.println("天気情報更新");
  }
}