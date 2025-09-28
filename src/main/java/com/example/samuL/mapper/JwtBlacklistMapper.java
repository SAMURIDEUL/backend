package com.example.samuL.mapper;

import com.example.samuL.dto.JwtBlacklistDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface JwtBlacklistMapper {
    public void insert(JwtBlacklistDto jwtBlacklistDto);
    public boolean existByToken(String token);
}
