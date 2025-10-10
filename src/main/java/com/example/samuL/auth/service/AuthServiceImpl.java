package com.example.samuL.auth.service;

import com.example.samuL.auth.dto.LoginRequestDto;
import com.example.samuL.auth.dto.LoginResponseDto;
import com.example.samuL.auth.dto.LoginUserDto;
import com.example.samuL.auth.dto.RefreshTokenDto;
import com.example.samuL.auth.jwt.JwtTokenProvider;
import com.example.samuL.auth.mapper.AuthMapper;
import com.example.samuL.auth.mapper.RefreshTokenMapper;
import com.example.samuL.dto.JwtBlacklistDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
public class AuthServiceImpl implements AuthService{
    @Autowired
    private AuthMapper authMapper;
    @Autowired
    private RefreshTokenMapper refreshTokenMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Override
    public LoginResponseDto login(LoginRequestDto loginRequestDto){
        LoginUserDto loginUserDto = authMapper.findEmail(loginRequestDto.getEmail());

        if(loginUserDto == null || !passwordEncoder.matches(loginRequestDto.getPassword_hash(), loginUserDto.getPassword_hash())){
            throw new RuntimeException("이메일이나 비밀번호가 맞지 않습니다");
        }

        String accessToken = jwtTokenProvider.CreateToken(loginUserDto.getEmail());
        String refreshToken = jwtTokenProvider.createRefreshToken(loginUserDto.getEmail());

        // 기존 refresh token 존재 시 삭제 (중복 방지)
        refreshTokenMapper.deleteByEmail(loginUserDto.getEmail());

        LocalDateTime expiryDate = jwtTokenProvider.getExpiration(refreshToken);

        // refresh token db 저장
        RefreshTokenDto refreshTokenDto = new RefreshTokenDto();
        refreshTokenDto.setEmail(loginUserDto.getEmail());
        refreshTokenDto.setRefreshToken(refreshToken);
        refreshTokenDto.setExpired_at(expiryDate);
        refreshTokenDto.setCreated_at(LocalDateTime.now());
        refreshTokenMapper.insert(refreshTokenDto);

        return new LoginResponseDto(accessToken, refreshToken);

    }
}
