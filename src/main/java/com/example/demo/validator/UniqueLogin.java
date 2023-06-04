package com.example.demo.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ユーザーのメールアドレス(ログインID)の重複があるかどうか判定する処理を実装させる
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueLoginValidator.class)
public @interface UniqueLogin {
  String message() default "このユーザーIDは既に登録されています。";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
}
