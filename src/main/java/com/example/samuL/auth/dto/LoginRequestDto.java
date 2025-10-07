package com.example.samuL.auth.dto;


import lombok.Data;

@Data
public class LoginRequestDto {
    private String email;
    private String password_hash;
}
