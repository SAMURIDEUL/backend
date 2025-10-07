package com.example.samuL.auth.service;

import com.example.samuL.auth.dto.LoginRequestDto;
import com.example.samuL.auth.dto.LoginUserDto;
import com.example.samuL.auth.jwt.JwtTokenProvider;
import com.example.samuL.auth.mapper.AuthMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class AuthServiceImpl implements AuthService{
    @Autowired
    private AuthMapper authMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Override
    public String login(LoginRequestDto loginRequestDto){
        LoginUserDto loginUserDto = authMapper.findEmail(loginRequestDto.getEmail());

        if(loginUserDto == null || !passwordEncoder.matches(loginRequestDto.getPassword_hash(), loginUserDto.getPassword_hash())){
            throw new RuntimeException("이메일이나 비밀번호가 맞지 않습니다");
        }
        return jwtTokenProvider.CreateToken(loginUserDto.getEmail());
    }
}
