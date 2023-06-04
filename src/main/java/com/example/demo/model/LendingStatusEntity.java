package com.example.demo.model;

import com.example.demo.util.LendingState;
import lombok.Data;

/**
 * 貸し出し状態を格納する
 */
@Data
public class LendingStatusEntity {
  private LendingState lendingState;
}
