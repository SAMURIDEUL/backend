package com.example.samuL.auth.service;

import com.example.samuL.auth.dto.LoginRequestDto;
import com.example.samuL.auth.dto.LoginResponseDto;


public interface AuthService {

    public LoginResponseDto login(LoginRequestDto loginRequestDto);
}
