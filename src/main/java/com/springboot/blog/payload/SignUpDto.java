package com.springboot.blog.payload;

import lombok.Data;

@Data
public class SignUpDto {
  private String username;
  private String email;
  private String password;
}