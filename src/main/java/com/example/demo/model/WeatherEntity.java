package com.example.demo.model;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class WeatherEntity {
  private LocalDateTime date;
  private String dateStr;
  private String weatherType;
  private String weatherdescription;
  private String humidity;
  private String maxTemperature;
  private String minTemperature;
}
