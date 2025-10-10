package com.example.samuL.JwtBlackList.mapper;

import com.example.samuL.JwtBlackList.dto.JwtBlacklistDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface JwtBlacklistMapper {
    public void insert(JwtBlacklistDto jwtBlacklistDto);
    public boolean existByToken(String token);
}
