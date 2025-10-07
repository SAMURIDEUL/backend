package com.example.samuL.auth.service;

import com.example.samuL.auth.dto.LoginRequestDto;



public interface AuthService {

    public String login(LoginRequestDto loginRequestDto);
}
