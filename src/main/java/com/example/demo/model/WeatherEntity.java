package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@Entity
@Table(name = "WEATHER")

public class WeatherEntity {
  @Id @Column(name = "id") private int id = 1;
  @Column(name = "date") private LocalDateTime date;
  @Column(name = "dateStr") private String dateStr;
  @Column(name = "weatherType") private String weatherType;
  @Column(name = "weatherdescription") private String weatherdescription;
  @Column(name = "humidity") private String humidity;
  @Column(name = "temperature") private String temperature;
  @Column(name = "maxTemperature") private String maxTemperature;
  @Column(name = "minTemperature") private String minTemperature;
}
