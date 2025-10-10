package com.example.samuL.auth.service;

import com.example.samuL.auth.dto.LoginRequestDto;
import com.example.samuL.auth.dto.LoginResponseDto;
import com.example.samuL.auth.dto.TokenResponseDto;


public interface AuthService {

    public LoginResponseDto login(LoginRequestDto loginRequestDto);
    public TokenResponseDto refreshAccessToken(String refreshToken);
}
