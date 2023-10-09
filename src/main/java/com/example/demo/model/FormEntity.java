package com.example.demo.model;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/**
 * 返却フォーム用に一時保存する
 */
@Data
public class FormEntity {
  private List<Integer> checks = new ArrayList<Integer>();
}
