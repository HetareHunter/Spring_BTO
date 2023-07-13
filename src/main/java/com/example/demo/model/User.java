package com.example.demo.model;

import com.example.demo.util.Authority;
import com.example.demo.validator.UniqueLogin;
import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/**
 * 本システムにログインできるユーザ、パスワードを管理する
 */
@Data
@Entity
@Table(name = "USERS")
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private int id;

  @NotNull
  @NotBlank
  @Size(max = 127)
  @Column(name = "first_name")
  private String first_name;

  @NotNull
  @NotBlank
  @Size(max = 127)
  @Column(name = "last_name")
  private String last_name;

  /**
   * ログインIDとなる値
   */
  @NotNull
  @NotBlank
  @Email
  @UniqueLogin
  @Column(name = "email")
  private String email;

  /**
   * ログイン時に使用するパスワード。BCryptPasswordEncoderを使い暗号化する
   */
  @NotNull
  @NotBlank
  @Size(max = 255)
  @Column(name = "password")
  private String password;

  /**
   * ユーザーの権限。現状ADMINとUSERの2つのみ
   */
  @NotNull @Column(name = "role") private Authority role;

  /**
   * 本の貸し借り状況
   */
  @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
  @Column(name = "lending")
  private List<Lending> lendings = new ArrayList<Lending>();

  @NotNull @Column(name = "created_at") private Timestamp created_at;

  @NotNull @Column(name = "updated_at") private Timestamp updated_at;

  private String name;
  private boolean admin;
}
