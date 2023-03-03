package com.example.demo.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    
    @Bean
    public Map<Integer, Integer> bookSelected() {
        return new HashMap<Integer, Integer>();
    }
}