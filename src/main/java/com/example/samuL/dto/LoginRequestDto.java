package com.example.samuL.dto;

import lombok.Data;

@Data
public class LoginRequestDto {
    private String email;
    private String password_hash;
}
