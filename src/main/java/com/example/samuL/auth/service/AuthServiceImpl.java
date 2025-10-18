package com.example.samuL.auth.service;

import com.example.samuL.JwtBlackList.service.JwtBlacklistService;
import com.example.samuL.auth.dto.*;
import com.example.samuL.auth.jwt.JwtTokenProvider;
import com.example.samuL.auth.mapper.AuthMapper;
import com.example.samuL.auth.mapper.RefreshTokenMapper;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
    @Autowired
    private JwtBlacklistService jwtBlacklistService;

    @Override
    public LoginResponseDto login(LoginRequestDto loginRequestDto){
        LoginUserDto loginUserDto = authMapper.findEmail(loginRequestDto.getEmail());

//        if(loginUserDto == null || !passwordEncoder.matches(loginRequestDto.getPassword_hash(), loginUserDto.getPassword_hash())){
//            throw new RuntimeException("이메일이나 비번이 틀렸습니다.");
//        }

        if(loginUserDto == null){
            throw new RuntimeException("이메일이 틀렸습니다.");
        }

        if(!passwordEncoder.matches(loginRequestDto.getPassword_hash(), loginUserDto.getPassword_hash())){
            throw new RuntimeException("비밀번호가 틀렸습니다.");
        }

        String accessToken = jwtTokenProvider.CreateToken(loginUserDto.getEmail());
        String refreshToken = jwtTokenProvider.createRefreshToken(loginUserDto.getEmail());

        // 기존 refresh token 존재 시 삭제 (중복 방지)
        refreshTokenMapper.deleteByEmail(loginUserDto.getEmail());

        // 만료 시간 계산
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

    @Override
    public TokenResponseDto refreshAccessToken(String refreshToken){
        // 토큰 유효성 확인
        if(!jwtTokenProvider.isValidateToken(refreshToken)){
            throw new RuntimeException("RefreshToken이 유효하지 않거나 만료되었습니다");
        }

        String email = jwtTokenProvider.extractEmail(refreshToken);

       RefreshTokenDto refreshTokenDto = refreshTokenMapper.findByEmail(email);
       if (refreshTokenDto == null || !refreshTokenDto.getRefreshToken().equals(refreshToken)){
           throw new RuntimeException("저장된 Refresh Token과 일치하지 않습니다");
       }

       //새로운 access token 발급
       String newAccessToken = jwtTokenProvider.CreateToken(email);

       return new TokenResponseDto(newAccessToken, refreshToken);
    }


    @Override
    public void logout(String token, String currentEmail){


        String email = jwtTokenProvider.extractEmail(token);
        if(!email.equals(currentEmail)){
            throw new AccessDeniedException("User mismatch. Cannot logout");
        }
        // refresh 토큰 삭제
        refreshTokenMapper.deleteByEmail(email);

        //access 토큰 블랙리스트 등록
        LocalDateTime expiration = jwtTokenProvider.getExpiration(token);
        jwtBlacklistService.addTokenToBlacklist(token, expiration);
    }
}
