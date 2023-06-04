package com.example.demo.validator;

import com.example.demo.repository.UserMngRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 重複したメールアドレス(ログインID)があるか判定する処理の実装
 */
public class UniqueLoginValidator
    implements ConstraintValidator<UniqueLogin, String> {
  private final UserMngRepository userRepository;

  public UniqueLoginValidator() { this.userRepository = null; }

  @Autowired
  public UniqueLoginValidator(UserMngRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    return userRepository == null ||
        userRepository.findByEmail(value).isEmpty();
  }
}
