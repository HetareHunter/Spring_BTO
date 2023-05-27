package com.example.demo.repository;

import com.example.demo.model.WeatherEntity;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WeatherRepository
    extends JpaRepository<WeatherEntity, Integer> {
  Optional<WeatherEntity> findByDate(LocalDateTime date);
}
