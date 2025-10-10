package com.example.samuL.JwtBlackList.service;


import com.example.samuL.JwtBlackList.dto.JwtBlacklistDto;
import com.example.samuL.JwtBlackList.mapper.JwtBlacklistMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class JwtBlacklistService {
    @Autowired
    private JwtBlacklistMapper jwtBlacklistMapper;

    public void addTokenToBlacklist(String token, LocalDateTime expired_at){
        JwtBlacklistDto jwtBlacklistDto = new JwtBlacklistDto();
        jwtBlacklistDto.setToken(token);
        jwtBlacklistDto.setExpired_at(expired_at);
        jwtBlacklistDto.setCreated_at(LocalDateTime.now());

        jwtBlacklistMapper.insert(jwtBlacklistDto);
    }

    public boolean isTokenBlacklisted(String token){
        return jwtBlacklistMapper.existByToken(token);
    }
}
