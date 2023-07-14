package com.example.demo.service;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
@Component
public class FileLoader {
  private final ResourceLoader resourceLoader;

  public FileLoader(ResourceLoader resourceLoader) {
    this.resourceLoader = resourceLoader;
  }

  public Resource load(String fileName) {
    return resourceLoader.getResource("classpath:static/data/" + fileName);
  }
}
